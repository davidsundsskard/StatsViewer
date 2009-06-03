package statsviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TestViewer extends ApplicationFrame {
	public TestViewer(String container) throws SQLException {
		super(container);

		Connection connection = null;
		try {
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/stats?user=stats&password=stats");

			final XYDataset dataset = createDataset(connection, container);
			final JFreeChart chart = createChart(dataset, container);
			final ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
			chartPanel.setMouseZoomable(true, false);
			setContentPane(chartPanel);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			connection.close();
		}
	}

	private JFreeChart createChart(final XYDataset dataset, String container) {

		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
				container, "Date", "Value", dataset, true, true, false);

		chart.setBackgroundPaint(Color.white);

		// final StandardLegend sl = (StandardLegend) chart.getLegend();
//		 sl.setDisplaySeriesShapes(true);

		final XYPlot plot = chart.getXYPlot();
		// plot.setOutlinePaint(null);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(false);

		final XYItemRenderer renderer = plot.getRenderer();
		if (renderer instanceof StandardXYItemRenderer) {
			final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
			// rr.setPlotShapes(true);
			rr.setShapesFilled(true);
			renderer.setSeriesStroke(0, new BasicStroke(2.0f));
			renderer.setSeriesStroke(1, new BasicStroke(2.0f));
		}

		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
		axis.setVerticalTickLabels(true);

		return chart;
	}

	private XYDataset createDataset(Connection connection, String containerName) throws SQLException {
		String query = "select cc.name, s.dtm, s.amount "
				+ "from statistics s "
				+ "join container c on s.containerId = c.id "
				+ "join category cc on s.categoryId = cc.id "
				+ "where c.name = ?";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, containerName);
			rs = stmt.executeQuery();
			
			Map<String, Map<Date, Integer>> map = new TreeMap<String, Map<Date,Integer>>();
			
			while(rs.next()) {
				String category = rs.getString(1);
				Date date = rs.getTimestamp(2);
				int amount = rs.getInt(3);
				
				if(map.containsKey(category)) {
					map.get(category).put(date, amount);
				} else {
					Map<Date, Integer> inner = new TreeMap<Date, Integer>();
					inner.put(date, amount);
					map.put(category, inner);
				}
			}
			
			return createDataSet(map);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} 
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				}
			}
		}
	}

	private XYDataset createDataSet(Map<String, Map<Date, Integer>> map) throws SQLException {
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
//		dataset.setDomainIsPointsInTime(true);
		
		for (Map.Entry<String, Map<Date, Integer>> categoryentry : map.entrySet()) {
			String category = categoryentry.getKey();
			final TimeSeries timeSeries = new TimeSeries(category);
			
			for (Map.Entry<Date, Integer> dateEntry : categoryentry.getValue().entrySet()) {
				Date date = dateEntry.getKey();
				Integer amount = dateEntry.getValue();
				System.out.println(date + ": " + amount);
				timeSeries.addOrUpdate(new Minute(date), amount);
			}
			dataset.addSeries(timeSeries);
		}

		return dataset;
	}

	public static void main(final String[] args) throws SQLException {

		final TestViewer demo = new TestViewer("ID SA DSL - by priority");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

}

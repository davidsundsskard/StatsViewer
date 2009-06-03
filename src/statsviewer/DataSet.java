package statsviewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSet {
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm");

	protected ArrayList<DataEntry> data = new ArrayList<DataEntry>();
	private ArrayList<String> categories = new ArrayList<String>();

	public DataSet(String containerName, boolean dayView) throws ParseException, SQLException,
			NamingException {
		System.out.println("Dayview: " + dayView);
		
		// Get a connection from JNDI context
		Context initCtx = new InitialContext();

		// TODO change this!
		DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/StatsDB");
		Connection connection = ds.getConnection();

		try {
			createDataSet(containerName, dayView, connection);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			connection.close();
		}
	}

	private void createDataSet(String containerName, boolean dayView, Connection connection)
			throws SQLException {
		// String query = "select cc.name, s.dtm, s.amount "
		// + "from statistics s "
		// + "join container c on s.containerId = c.id "
		// + "join category cc on s.categoryId = cc.id "
		// + "where c.name = ?";

		String query; 
		if(dayView)
			query = "select cat.name, date_format(s.dtm, '%Y-%m-%d'), s.amount "
				+ "from Statistics s "
				+ "join Category cat on s.categoryId = cat.id "
				+ "join Container cont on cat.containerId = cont.id "
				+ "where cont.name = ? "
				+ "and s.dtm in ( "
				+ "select max(s.dtm) "
				+ "from Statistics s "
				+ "group by date_format(s.dtm, '%Y-%m-%d') " + ");";
		else 
			query = "select cat.name, s.dtm, s.amount "
				+ "from Statistics s "
				+ "join Category cat on s.categoryId = cat.id "
				+ "join Container cont on cat.containerId = cont.id "
				+ "where cont.name = ?";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, containerName);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String category = rs.getString(1);
				Date date = rs.getTimestamp(2);
				int amount = rs.getInt(3);

				if (!categories.contains(category))
					categories.add(category);

				data.add(new DataEntry(date, category, amount));
			}

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

	public ArrayList<DataEntry> getDataByDate(String filterSection) {
		ArrayList<DataEntry> results = new ArrayList<DataEntry>();
		Map<Date, Integer> dateMap = new HashMap<Date, Integer>();

		int currentPosition = 0;
		for (DataEntry dataEntry : data) {
			if (filterSection == null ? true : filterSection.equals(dataEntry
					.getCategory())) {
				Integer position = (Integer) dateMap.get(dataEntry.getDate());
				if (position == null) {
					results.add(dataEntry);
					dateMap.put(dataEntry.getDate(), new Integer(
							currentPosition));
					currentPosition++;
				} else {
					DataEntry previousWebHit = (DataEntry) results.get(position
							.intValue());
					previousWebHit.setAmount(previousWebHit.getAmount()
							+ dataEntry.getAmount());
				}
			}

		}
		return results;
	}

	public ArrayList<DataEntry> getDataBySection(Date filterDate)
			throws ParseException {
		ArrayList<DataEntry> results = new ArrayList<DataEntry>();
		Map<String, Integer> sectionMap = new HashMap<String, Integer>();

		int currentPosition = 0;
		for (DataEntry dataEntry : data) {
			// Strip millis from date
			Date entryDate = dataEntry.getDate();
			if (entryDate != null) {
				entryDate = dateFormat.parse(dateFormat.format(entryDate));
			}

			if (filterDate == null ? true : filterDate.equals(entryDate)) {
				Integer position = (Integer) sectionMap.get(dataEntry
						.getCategory());
				if (position == null) {
					results.add(dataEntry);
					sectionMap.put(dataEntry.getCategory(), new Integer(
							currentPosition));
					currentPosition++;
				} else {
					DataEntry previousWebHit = (DataEntry) results.get(position
							.intValue());
					previousWebHit.setAmount(previousWebHit.getAmount()
							+ dataEntry.getAmount());
				}
			}
		}
		return results;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public static void main(java.lang.String[] args) {
	}
}
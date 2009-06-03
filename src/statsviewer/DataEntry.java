package statsviewer;

import java.util.Date;

public class DataEntry {
	protected Date date = null;
	protected String category = null;
	protected long amount = 0;

    public DataEntry(Date dHitDate, String sSection, long lHitCount) {
		this.date = dHitDate;
		this.category = sSection;
		this.amount = lHitCount;
    }

	public Date getDate() {
		return this.date;
	}
	public String getCategory() {
		return this.category;
	}
	public long getAmount() {
		return this.amount;
	}

	public void setDate(Date dHitDate) {
		this.date = dHitDate;
	}
	public void setCategory(String sSection) {
		this.category = sSection;
	}
	public void setAmount(long lHitCount) {
		this.amount = lHitCount;
	}

}
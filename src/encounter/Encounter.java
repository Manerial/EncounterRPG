package encounter;

import java.math.BigDecimal;

/**
 * Store the amount of creature on day or night
 * 
 * @author JHER
 *
 */
public class Encounter {
	private BigDecimal day;
	private BigDecimal night;
	
	public Encounter(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
		this.day = bigDecimal;
		this.night = bigDecimal2;
	}

	public BigDecimal getDay() {
		return day;
	}
	
	public void setDay(BigDecimal day) {
		this.day = day;
	}

	public BigDecimal getNight() {
		return night;
	}
	
	public void setNight(BigDecimal night) {
		this.night = night;
	}
}

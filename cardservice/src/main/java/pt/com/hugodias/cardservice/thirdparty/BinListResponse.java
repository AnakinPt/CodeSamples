package pt.com.hugodias.cardservice.thirdparty;

public class BinListResponse {
	private BinListNumber number;
	private String scheme;
	private String type;
	private String brand;
	private boolean prepaid;
	private BinListCountry country;
	private BinListBank bank;
	
	
	
	public BinListNumber getNumber() {
		return number;
	}

	public void setNumber(BinListNumber number) {
		this.number = number;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public boolean isPrepaid() {
		return prepaid;
	}

	public void setPrepaid(boolean prepaid) {
		this.prepaid = prepaid;
	}

	public BinListCountry getCountry() {
		return country;
	}

	public void setCountry(BinListCountry country) {
		this.country = country;
	}

	public BinListBank getBank() {
		return bank;
	}

	public void setBank(BinListBank bank) {
		this.bank = bank;
	}

	public class BinListNumber{
		private int length;
		private boolean luhn;
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public boolean isLuhn() {
			return luhn;
		}
		public void setLuhn(boolean luhn) {
			this.luhn = luhn;
		}
		
		
	}
	
	public class BinListCountry{
		private String numeric;
		private String alpha2;
		private String name;
		private String emoji;
		private String currency;
		private int latitude;
		private int longitude;
		public String getNumeric() {
			return numeric;
		}
		public void setNumeric(String numeric) {
			this.numeric = numeric;
		}
		public String getAlpha2() {
			return alpha2;
		}
		public void setAlpha2(String alpha2) {
			this.alpha2 = alpha2;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmoji() {
			return emoji;
		}
		public void setEmoji(String emoji) {
			this.emoji = emoji;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public int getLatitude() {
			return latitude;
		}
		public void setLatitude(int latitude) {
			this.latitude = latitude;
		}
		public int getLongitude() {
			return longitude;
		}
		public void setLongitude(int longitude) {
			this.longitude = longitude;
		}
		
		
	}
	
	public class BinListBank{
		private String name;
		private String url;
		private String phone;
		private String city;
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		
		
	}
}

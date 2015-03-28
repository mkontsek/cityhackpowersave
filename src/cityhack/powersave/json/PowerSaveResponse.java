package cityhack.powersave.json;

public class PowerSaveResponse {
	public int status;
	public String description;
	
	public PowerSaveResponse(int status, String description) {
		this.status = status;
		this.description = description;
	}
}
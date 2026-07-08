package worker;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Task implements Serializable{
	
	private String imgPath;
	private byte[] imgBytes;
	
	private String logoPath;
	private byte[] logoBytes;

	private String tipoProcura;
	
	private int idClient;
	
	public Task(String imgPath, byte[] imgBytes, String logoPath, byte[] logoBytes, String tipoProcura){
		this.imgPath = imgPath;
		this.imgBytes = imgBytes;
		this.logoPath = logoPath;
		this.logoBytes = logoBytes;
		this.tipoProcura = tipoProcura;
	}

	public String getImgPath(){
		return imgPath;
	}

	public void setImgPath(String imgPath){
		this.imgPath = imgPath;
	}

	public byte[] getImgBytes() {
		return imgBytes;
	}

	public void setImgBytes(byte[] imgBytes){
		this.imgBytes = imgBytes;
	}

	public String getLogoPath(){
		return logoPath;
	}

	public void setLogoPath(String logoPath){
		this.logoPath = logoPath;
	}

	public byte[] getLogoBytes(){
		return logoBytes;
	}

	public void setLogoBytes(byte[] logoBytes){
		this.logoBytes = logoBytes;
	}

	public String getTipoProcura(){
		return tipoProcura;
	}

	public void setTipoProcura(String tipoProcura){
		this.tipoProcura = tipoProcura;
	}
	
	public int getIDClient(){
		return idClient;
	}
	
	public void setIDClient(int idClient){
		this.idClient = idClient;
	}

	public String toString(){
		return "Task [imgPath=" + imgPath + ", imgBytes=" + Arrays.toString(imgBytes) + ", logoPath=" + logoPath
				+ ", logoBytes=" + Arrays.toString(logoBytes) + ", tipoProcura=" + tipoProcura + ", idClient="
				+ idClient + "]";
	}

}

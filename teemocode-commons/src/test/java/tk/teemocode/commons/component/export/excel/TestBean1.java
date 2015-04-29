package tk.teemocode.commons.component.export.excel;

import java.util.Date;

public class TestBean1 implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String vehicleNo;

	private TestBean2 bean2;

	private Date regDate;

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public TestBean2 getBean2() {
		return bean2;
	}

	public void setBean2(TestBean2 bean2) {
		this.bean2 = bean2;
	}
}
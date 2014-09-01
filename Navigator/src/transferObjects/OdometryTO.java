package transferObjects;

public class OdometryTO {
	private double TwistLinearX;
	private double TwistLinearY;
	private double TwistLinearZ;
	
	private double TwistAngularX;
	private double TwistAngularY;
	private double TwistAngularZ;
	
	private double PoseLinearX;
	private double PoseLinearY;
	private double PoseLinearZ;
	
	private double PoseAngularX;
	private double PoseAngularY;
	private double PoseAngularZ;
	private double PoseAngularW;
	
	private String frameID;
	private int seqNumber;
	
	private TimeStampTO timestamp;


	public OdometryTO(){
		this.timestamp = new TimeStampTO();
	}

	
	public TimeStampTO getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(TimeStampTO timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getFrameID() {
		return frameID;
	}

	public void setFrameID(String frameID) {
		this.frameID = frameID;
	}

	public int getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	
	public double getTwistLinearX() {
		return TwistLinearX;
	}
	public void setTwistLinearX(double twistLinearX) {
		TwistLinearX = twistLinearX;
	}
	public double getTwistLinearY() {
		return TwistLinearY;
	}
	public void setTwistLinearY(double twistLinearY) {
		TwistLinearY = twistLinearY;
	}
	public double getTwistLinearZ() {
		return TwistLinearZ;
	}
	public void setTwistLinearZ(double twistLinearZ) {
		TwistLinearZ = twistLinearZ;
	}
	public double getTwistAngularX() {
		return TwistAngularX;
	}
	public void setTwistAngularX(double twistAngularX) {
		TwistAngularX = twistAngularX;
	}
	public double getTwistAngularY() {
		return TwistAngularY;
	}
	public void setTwistAngularY(double twistAngularY) {
		TwistAngularY = twistAngularY;
	}
	public double getTwistAngularZ() {
		return TwistAngularZ;
	}
	public void setTwistAngularZ(double twistAngularZ) {
		TwistAngularZ = twistAngularZ;
	}
	public double getPoseLinearX() {
		return PoseLinearX;
	}
	public void setPoseLinearX(double poseLinearX) {
		PoseLinearX = poseLinearX;
	}
	public double getPoseLinearY() {
		return PoseLinearY;
	}
	public void setPoseLinearY(double poseLinearY) {
		PoseLinearY = poseLinearY;
	}
	public double getPoseLinearZ() {
		return PoseLinearZ;
	}
	public void setPoseLinearZ(double poseLinearZ) {
		PoseLinearZ = poseLinearZ;
	}
	public double getPoseAngularX() {
		return PoseAngularX;
	}
	public void setPoseAngularX(double poseAngularX) {
		PoseAngularX = poseAngularX;
	}
	public double getPoseAngularY() {
		return PoseAngularY;
	}
	public void setPoseAngularY(double poseAngularY) {
		PoseAngularY = poseAngularY;
	}
	public double getPoseAngularZ() {
		return PoseAngularZ;
	}
	public void setPoseAngularZ(double poseAngularZ) {
		PoseAngularZ = poseAngularZ;
	}
	public double getPoseAngularW() {
		return PoseAngularW;
	}
	public void setPoseAngularW(double poseAngularW) {
		PoseAngularW = poseAngularW;
	}

	


}

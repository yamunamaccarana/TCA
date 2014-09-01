package messageSerializer;

public class MessageSerializer {
	
	public static String messageToString(sensor_msgs.LaserScan msg){
		String returnedString = null;
		returnedString += "  Time: " + msg.getHeader().getStamp().secs + " s, " + msg.getHeader().getStamp().nsecs + " ns" + "\n";
		returnedString += "Angle min: " + msg.getAngleMin() + "\n";
		returnedString += "Angle max: " + msg.getAngleMax() + "\n";
		returnedString += "Range min: " + msg.getRangeMin() + "\n";
		returnedString += "Range max: " + msg.getRangeMax() + "\n";
		returnedString += "Scan Time: " + msg.getScanTime() + "\n";
		returnedString += "Ranges: \n";
		System.out.println("Ranges: ");
		for(int i = 0; i < msg.getRanges().length; i++){
			returnedString += msg.getRanges()[i] + " ";
		}
		returnedString += "\n";
	
		return returnedString;
	}

	public static String messageToString(nav_msgs.Odometry msg){
		String returnedString = null;
		returnedString += "  Time: " + msg.getHeader().getStamp().secs + " s, " + msg.getHeader().getStamp().nsecs + " ns" + "\n";
		returnedString += "  Pose:\n";
		returnedString += "    Linear:\n";
		returnedString += "      X="+msg.getPose().getPose().getPosition().getX() + "\n";
		returnedString += "      Y="+msg.getPose().getPose().getPosition().getY() + "\n";
		returnedString += "      Z="+msg.getPose().getPose().getPosition().getZ() + "\n";
		returnedString += "    Rotation:\n";
		returnedString += "      X="+msg.getPose().getPose().getOrientation().getX() + "\n";
		returnedString += "      Y="+msg.getPose().getPose().getOrientation().getY() + "\n";
		returnedString += "      Z="+msg.getPose().getPose().getOrientation().getZ() + "\n";
		returnedString += "      W="+msg.getPose().getPose().getOrientation().getW() + "\n";
		returnedString += "  Twist:\n";
		returnedString += "    Linear:\n";
		returnedString += "      X="+msg.getTwist().getTwist().getLinear().getX() + "\n";
		returnedString += "      Y="+msg.getTwist().getTwist().getLinear().getY() + "\n";
		returnedString += "      Z="+msg.getTwist().getTwist().getLinear().getZ() + "\n";
		returnedString += "    Rotation:\n";
		returnedString += "      X="+msg.getTwist().getTwist().getAngular().getX() + "\n";
		returnedString += "      Y="+msg.getTwist().getTwist().getAngular().getY() + "\n";
		returnedString += "      Z="+msg.getTwist().getTwist().getAngular().getZ() + "\n";
		
		return returnedString;
	}
	
	public static String messageToString(std_msgs.String msg){
		return msg.getData();
	}
}

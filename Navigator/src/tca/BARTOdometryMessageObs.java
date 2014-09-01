package tca;

import java.util.Observable;
import java.util.Observer;

import transferObjects.OdometryTO;

public class BARTOdometryMessageObs implements Observer {
	
	MessageManager msgMan;
	
	public BARTOdometryMessageObs(MessageManager msgMan){
		this.msgMan = msgMan;
	}

	@Override
	public void update(Observable o, Object arg) {
		//Get the message
		nav_msgs.Odometry inOdo = (nav_msgs.Odometry) arg;
		
		//set fields to TO
		OdometryTO outOdo = new OdometryTO();
		outOdo.setPoseLinearX(inOdo.getPose().getPose().getPosition().getX());
		outOdo.setPoseLinearY(inOdo.getPose().getPose().getPosition().getY());
		outOdo.setPoseLinearZ(inOdo.getPose().getPose().getPosition().getZ());
		
		outOdo.setPoseAngularX(inOdo.getPose().getPose().getOrientation().getX());
		outOdo.setPoseAngularY(inOdo.getPose().getPose().getOrientation().getY());
		outOdo.setPoseAngularZ(inOdo.getPose().getPose().getOrientation().getZ());
		outOdo.setPoseAngularW(inOdo.getPose().getPose().getOrientation().getW());
		
		outOdo.setTwistLinearX(inOdo.getTwist().getTwist().getLinear().getX());
		outOdo.setTwistLinearY(inOdo.getTwist().getTwist().getLinear().getY());
		outOdo.setTwistLinearZ(inOdo.getTwist().getTwist().getLinear().getZ());
		
		outOdo.setTwistAngularX(inOdo.getTwist().getTwist().getAngular().getX());
		outOdo.setTwistAngularY(inOdo.getTwist().getTwist().getAngular().getY());
		outOdo.setTwistAngularZ(inOdo.getTwist().getTwist().getAngular().getZ());
			
		outOdo.setFrameID(inOdo.getHeader().getFrameId());
		outOdo.setSeqNumber(inOdo.getHeader().getSeq());
		outOdo.getTimestamp().secs = inOdo.getHeader().getStamp().secs;
		outOdo.getTimestamp().nsec = inOdo.getHeader().getStamp().nsecs;		
		
		//Add the message to memory
		msgMan.addOdometryMessage(outOdo);
	}


}



package stellarium.detector;

import net.minecraftforge.client.IRenderHandler;
import stellarium.view.EnumEyeCCD;

public class DetectorEye implements IDetector {

	public void render(float partialTicks) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public EnumEyeCCD type() {
		return EnumEyeCCD.Eye;
	}

}

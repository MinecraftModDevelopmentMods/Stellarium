package stellarium;


import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.*;

import org.lwjgl.opengl.*;

import stellarium.stellars.*;
import stellarium.stellars.background.BrStar;
import stellarium.util.math.Spmath;
import stellarium.util.math.Transforms;
import stellarium.util.math.Vec;
import stellarium.util.math.Vecf;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DrawSky extends IRenderHandler {
	
	private TextureManager renderEngine;
	private WorldClient world;
	private Minecraft mc;
	private Random random;
	private Tessellator tessellator1=Tessellator.instance;
	
    private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation locationSunPng = new ResourceLocation("stellarium", "stellar/halo.png");
    private static final ResourceLocation locationMoonPng = new ResourceLocation("stellarium", "stellar/lune.png");
    private static final ResourceLocation locationStarPng = new ResourceLocation("stellarium", "stellar/star.png");
    private static final ResourceLocation locationhalolunePng = new ResourceLocation("stellarium", "stellar/haloLune.png");

    
	/*private boolean IsMid, IsCalcd;*/
	
	public DrawSky(){
		random = new Random(System.currentTimeMillis());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float par1, WorldClient world, Minecraft mc) {
		
		this.renderEngine=mc.renderEngine;
		this.world=world;
		this.mc=mc;
		
		if (mc.theWorld.provider.dimensionId == 1)
        {
            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.disableStandardItemLighting();
            GL11.glDepthMask(false);
            renderEngine.bindTexture(locationEndSkyPng);
            Tessellator tessellator = Tessellator.instance;

            for (int i = 0; i < 6; ++i)
            {
                GL11.glPushMatrix();

                if (i == 1)
                {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 2)
                {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 3)
                {
                    GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 4)
                {
                    GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                }

                if (i == 5)
                {
                    GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                }

                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_I(2631720);
                tessellator.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
                tessellator.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D);
                tessellator.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D, 16.0D);
                tessellator.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D, 0.0D);
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            
            double time=(double)world.getWorldTime()+par1;
            
            StellarManager.Update(time, mc.theWorld.provider.isSurfaceWorld());
            this.RenderStar(0.0f, 0.0f, time);
        }
        else if (mc.theWorld.provider.isSurfaceWorld())
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Vec3 vec3 = world.getSkyColor(mc.renderViewEntity, par1);
            float f1 = (float)vec3.xCoord;
            float f2 = (float)vec3.yCoord;
            float f3 = (float)vec3.zCoord;
            float f4;

            if (mc.gameSettings.anaglyph)
            {
                float f5 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
                float f6 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
                f4 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
                f1 = f5;
                f2 = f6;
                f3 = f4;
            }

            GL11.glColor3f(f1, f2, f3);
            Tessellator tessellator1 = Tessellator.instance;
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.disableStandardItemLighting();
            float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(par1), par1);
            float f7;
            float f8;
            float f9;
            float f10;

            if (afloat != null)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glShadeModel(GL11.GL_SMOOTH);
                GL11.glPushMatrix();
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(MathHelper.sin(world.getCelestialAngleRadians(par1)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                f4 = afloat[0];
                f7 = afloat[1];
                f8 = afloat[2];
                float f11;

                if (mc.gameSettings.anaglyph)
                {
                    f9 = (f4 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
                    f10 = (f4 * 30.0F + f7 * 70.0F) / 100.0F;
                    f11 = (f4 * 30.0F + f8 * 70.0F) / 100.0F;
                    f4 = f9;
                    f7 = f10;
                    f8 = f11;
                }

                tessellator1.startDrawing(6);
                tessellator1.setColorRGBA_F(f4, f7, f8, afloat[3]);
                tessellator1.addVertex(0.0D, 100.0D, 0.0D);
                byte b0 = 16;
                tessellator1.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0.0F);

                for (int j = 0; j <= b0; ++j)
                {
                    f11 = (float)j * (float)Math.PI * 2.0F / (float)b0;
                    float f12 = MathHelper.sin(f11);
                    float f13 = MathHelper.cos(f11);
                    tessellator1.addVertex((double)(f12 * 120.0F), (double)(f13 * 120.0F), (double)(-f13 * 40.0F * afloat[3]));
                }

                tessellator1.draw();
                GL11.glPopMatrix();
                GL11.glShadeModel(GL11.GL_FLAT);
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            
            GL11.glPushMatrix();
            f4 = 1.0F - world.getRainStrength(par1);
            f7 = 0.0F;
            f8 = 0.0F;
            f9 = 0.0F;
            
            float bglight=f1+f2+f3;
            
           
            GL11.glTranslatef(f7, f8, f9); //e,z,s
            GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); //e,n,z
//            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F); // s,z,w
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
            
            
            double time=(double)world.getWorldTime()+par1;
            
            StellarManager.Update(time, mc.theWorld.provider.isSurfaceWorld());
            
            /*if(par1>0.5 && !IsMid){
            	IsMid=true;
            	IsCalcd=false;
            }
            
            if(StellarManager.Earth.EcRPos==null || !IsCalcd)
            {
            	StellarManager.Update(time, mc.theWorld.provider.isSurfaceWorld());
            	IsCalcd=true;
            }*/
            
            this.RenderStar(bglight, f4, time);
            
           
            
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
           
           /* GL11.glRotatef(-(float)(90-(Transforms.Lat*180.0/Math.PI)), 1.0f, 0.0f, 0.0f);
            GL11.glRotatef((float)(-90.0-Transforms.Rot*time*180.0/Math.PI), 0.0f, 0.0f, 1.0f);//?,Sp,p
            GL11.glRotatef(-(float) (Transforms.e*180.0/Math.PI), 0.0f, 1.0f, 0.0f);
    		GL11.glRotatef((float)(Transforms.Prec*time*180.0/Math.PI), 0.0f, 0.0f, 1.0f);
            GL11.glRotatef((float) (Transforms.e*180.0/Math.PI), 0.0f, 1.0f, 0.0f);*/

//            GL11.glRotatef(world.getCelestialAngle(par1) * 360.0F, 1.0F, 0.0F, 0.0F);
            
            
            //Rendering Sun
            f10 = 30.0F;
 
            Vec pos=StellarManager.Sun.GetPosition();
            double size=StellarManager.Sun.Radius/pos.Size()*99.0*20;
            pos=Vec.Div(pos, pos.Size());
        	Vec dif=Vec.Cross(pos, new Vec(0.0,0.0,1.0));
        	dif=Vec.Mul(dif, size/dif.Size());
        	Vec dif2=Vec.Cross(dif, pos);
        	pos=Vec.Mul(pos, 99.0);
       
        	
            renderEngine.bindTexture(this.locationSunPng);
            tessellator1.startDrawingQuads();
        	tessellator1.addVertexWithUV(pos.x+dif.x, pos.y+dif.y, pos.z+dif.z,0.0,0.0);
        	tessellator1.addVertexWithUV(pos.x+dif2.x, pos.y+dif2.y, pos.z+dif2.z,1.0,0.0);
        	tessellator1.addVertexWithUV(pos.x-dif.x, pos.y-dif.y, pos.z-dif.z,1.0,1.0);
        	tessellator1.addVertexWithUV(pos.x-dif2.x, pos.y-dif2.y, pos.z-dif2.z,0.0,1.0);
/*            tessellator1.addVertexWithUV((double)(-f10), 100.0D, (double)(-f10), 0.0D, 0.0D);
            tessellator1.addVertexWithUV((double)f10, 100.0D, (double)(-f10), 1.0D, 0.0D);
            tessellator1.addVertexWithUV((double)f10, 100.0D, (double)f10, 1.0D, 1.0D);
            tessellator1.addVertexWithUV((double)(-f10), 100.0D, (double)f10, 0.0D, 1.0D);*/
            tessellator1.draw();
            //Sun
            
            //Rendering Moon
            
            int latn=StellarManager.ImgFrac, longn=2*StellarManager.ImgFrac;
            Vecf moonvec[][];
            double moonilum[][];
            moonvec=new Vecf[longn][latn+1];
            moonilum=new double[longn][latn+1];
            Vec Buf;
            int latc, longc;
            for(longc=0; longc<longn; longc++){
            	for(latc=0; latc<=latn; latc++){
            		Buf=StellarManager.Moon.PosLocalM((double)longc/(double)longn*360.0, (double)latc/(double)latn*180.0-90.0, Transforms.time);
            		moonilum[longc][latc]=StellarManager.Moon.Illumination(Buf);
            		Buf=StellarManager.Moon.PosLocalG(Buf);
            		Buf=Vec.Mul(Buf, 50000.0);
            		Vecf Buff=new Vecf((float)Buf.x,(float)Buf.y,(float)Buf.z);
            		Buff=Transforms.ZTEctoNEcf.Rot(Buff);
            		Buff=Transforms.EctoEqf.Rot(Buff);
            		Buff=Transforms.NEqtoREqf.Rot(Buff);
            		Buff=Transforms.REqtoHorf.Rot(Buff);
            		Buff=ExtinctionRefraction.Refraction(Buff, true);
            		moonvec[longc][latc]=Buff;
            		if(Buff.z<0.0f) moonilum[longc][latc]=0.0;

            	}
            }
          
            
            
            
            f10 = 20.0F;
            
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            
            renderEngine.bindTexture(locationhalolunePng);
            /*int k = world.getMoonPhase();
            int l = k % 4;
            int i1 = k / 4 % 2;
            float f14 = (float)(l + 0) / 4.0F;
            float f15 = (float)(i1 + 0) / 2.0F;
            float f16 = (float)(l + 1) / 4.0F;
            float f17 = (float)(i1 + 1) / 2.0F;*/
          
            
            Vec posm=StellarManager.Moon.GetPosition();
            
            posm=ExtinctionRefraction.Refraction(posm, true);
            
            if(posm.z>0.0f){
            double sizem=StellarManager.Moon.Radius/posm.Size()*98.0*5.0;
            posm=Vec.Div(posm, posm.Size());
        	Vec difm=Vec.Cross(posm, new Vec(0.0,0.0,1.0));
        	difm=Vec.Mul(difm, sizem/difm.Size());
        	Vec difm2=Vec.Cross(difm, posm);
        	posm=Vec.Mul(posm, 98.0);
        	
        	float alpha=Optics.GetAlphaFromMagnitude(-17.0-StellarManager.Moon.Mag,bglight);
        	
            GL11.glColor4d(1.0, 1.0, 1.0, f4*alpha);
            
            tessellator1.startDrawingQuads();
        	tessellator1.addVertexWithUV(posm.x+difm.x, posm.y+difm.y, posm.z+difm.z,0.0,0.0);
        	tessellator1.addVertexWithUV(posm.x+difm2.x, posm.y+difm2.y, posm.z+difm2.z,0.0,1.0);
        	tessellator1.addVertexWithUV(posm.x-difm.x, posm.y-difm.y, posm.z-difm.z,1.0,1.0);
        	tessellator1.addVertexWithUV(posm.x-difm2.x, posm.y-difm2.y, posm.z-difm2.z,1.0,0.0);
        	tessellator1.draw();
            }
        	
        	
            renderEngine.bindTexture(locationMoonPng);
            
          
            
            for(longc=0; longc<longn; longc++){
            	for(latc=0; latc<latn; latc++){
            		
            		int longcd=(longc+1)%longn;
            		double longd=(double)longc/(double)longn;
            		double latd=1.0-(double)latc/(double)latn;
            		double longdd=(double)longcd/(double)longn;
            		double latdd=1.0-(double)(latc+1)/(double)latn;
            		
                    GL11.glColor4d(1.0, 1.0, 1.0, (f4*moonilum[longc][latc]-4.0f*bglight)*2.0f);
                	
                    tessellator1.startDrawingQuads();
                    tessellator1.addVertexWithUV(moonvec[longc][latc].x, moonvec[longc][latc].y, moonvec[longc][latc].z, Spmath.fmod(longd+0.5, 1.0), latd);
                	tessellator1.addVertexWithUV(moonvec[longcd][latc].x, moonvec[longcd][latc].y, moonvec[longcd][latc].z, Spmath.fmod(longdd+0.5,1.0), latd);
                	tessellator1.addVertexWithUV(moonvec[longcd][latc+1].x, moonvec[longcd][latc+1].y, moonvec[longcd][latc+1].z, Spmath.fmod(longdd+0.5, 1.0), latdd);
                	tessellator1.addVertexWithUV(moonvec[longc][latc+1].x, moonvec[longc][latc+1].y, moonvec[longc][latc+1].z, Spmath.fmod(longd+0.5,1.0), latdd);
                    tessellator1.draw();
            	}
            }
        	
            
           /* tessellator1.startDrawingQuads();
            tessellator1.addVertexWithUV((double)(-f10), -100.0D, (double)1.0, (double)f16, (double)f17);
            tessellator1.addVertexWithUV((double)f10, -100.0D, (double)f10, (double)f14, (double)f17);
            tessellator1.addVertexWithUV((double)f10, -100.0D, (double)(-f10), (double)f14, (double)f15);
            tessellator1.addVertexWithUV((double)(-f10), -100.0D, (double)(-f10), (double)f16, (double)f15);
            tessellator1.draw();*/
            //Moon
            
            renderEngine.bindTexture(locationStarPng);
            this.DrawStellarObj(f1+f2+f3, f4, StellarManager.Mercury.AppPos,StellarManager.Mercury.App_Mag);
            this.DrawStellarObj(f1+f2+f3, f4, StellarManager.Venus.AppPos,StellarManager.Venus.App_Mag);
            this.DrawStellarObj(f1+f2+f3, f4, StellarManager.Mars.AppPos,StellarManager.Mars.App_Mag);
            this.DrawStellarObj(f1+f2+f3, f4, StellarManager.Jupiter.AppPos,StellarManager.Jupiter.App_Mag);
            this.DrawStellarObj(f1+f2+f3, f4, StellarManager.Saturn.AppPos,StellarManager.Saturn.App_Mag);
            this.DrawStellarObj(f1+f2+f3, f4, StellarManager.Uranus.AppPos,StellarManager.Uranus.App_Mag);
            this.DrawStellarObj(f1+f2+f3, f4, StellarManager.Neptune.AppPos,StellarManager.Neptune.App_Mag);
            
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            float f18 = world.getStarBrightness(par1) * f4;


            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor3f(0.0F, 0.0F, 0.0F);
            double d0 = mc.thePlayer.getPosition(par1).yCoord - world.getHorizon();

            if (d0 < 0.0D)
            {
                f8 = 1.0F;
                f9 = -((float)(d0 + 65.0D));
                f10 = -f8;
                tessellator1.startDrawingQuads();
                tessellator1.setColorRGBA_I(0, 255);
                tessellator1.addVertex((double)(-f8), (double)f9, (double)f8);
                tessellator1.addVertex((double)f8, (double)f9, (double)f8);
                tessellator1.addVertex((double)f8, (double)f10, (double)f8);
                tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
                tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
                tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
                tessellator1.addVertex((double)f8, (double)f9, (double)(-f8));
                tessellator1.addVertex((double)(-f8), (double)f9, (double)(-f8));
                tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
                tessellator1.addVertex((double)f8, (double)f10, (double)f8);
                tessellator1.addVertex((double)f8, (double)f9, (double)f8);
                tessellator1.addVertex((double)f8, (double)f9, (double)(-f8));
                tessellator1.addVertex((double)(-f8), (double)f9, (double)(-f8));
                tessellator1.addVertex((double)(-f8), (double)f9, (double)f8);
                tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
                tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
                tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
                tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
                tessellator1.addVertex((double)f8, (double)f10, (double)f8);
                tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
                tessellator1.draw();
            }

            if (world.provider.isSkyColored())
            {
                GL11.glColor3f(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
            }
            else
            {
                GL11.glColor3f(f1, f2, f3);
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
        }
		
	}
	
	public void RenderStar(float bglight, float weathereff, double time){

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		
		renderEngine.bindTexture(locationStarPng);
   
        
        if(!world.provider.isHellWorld){
        for(int i=0; i<BrStar.NumStar; i++){
        	if(BrStar.stars[i].unable) continue;
//        	GL11.((7-Star.stars[i].Mag)*30.0f,(7-Star.stars[i].Mag)*30.0f,(7-Star.stars[i].Mag)*30.0f);
        	
        	BrStar star=BrStar.stars[i];
        	
        	Vecf pos=star.AppPos;
        	float Mag=star.App_Mag;
        	float B_V=star.App_B_V;
        	float Turb=0.2f*(float) random.nextGaussian();
        	Mag+=Turb;
        	
        	if(Mag > StellarManager.Mag_Limit)
        		continue;
        	if(pos.z<0) continue;
        	
        	float size=0.3f;
        	float alpha=Optics.GetAlphaFromMagnitude(Mag, bglight);
        	
        	Vecf dif=Vecf.Cross(pos, new Vecf(0.0f,0.0f,1.0f));
        	dif=Vecf.Mul(dif, size/dif.Size());
        	Vecf dif2=Vecf.Cross(dif, pos);
        	pos=Vecf.Mul(pos, 100.0f);
        	
        	Color c=Color.GetColor(B_V);
   

            tessellator1.startDrawingQuads();

        	GL11.glColor4f(((float)c.r)/255.0f, ((float)c.g)/255.0f, ((float)c.b)/255.0f, weathereff*alpha);
        	
        	
        	tessellator1.addVertexWithUV(pos.x+dif.x, pos.y+dif.y, pos.z+dif.z,0.0,0.0);
        	tessellator1.addVertexWithUV(pos.x+dif2.x, pos.y+dif2.y, pos.z+dif2.z,1.0,0.0);
        	tessellator1.addVertexWithUV(pos.x-dif.x, pos.y-dif.y, pos.z-dif.z,1.0,1.0);
        	tessellator1.addVertexWithUV(pos.x-dif2.x, pos.y-dif2.y, pos.z-dif2.z,0.0,1.0);
        	
            tessellator1.draw();
            
        }
        }

	}

	public void DrawStellarObj(float bglight, float weathereff, Vec pos, double Mag){
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		
		if(Mag > StellarManager.Mag_Limit) return;
		if(pos.z<0) return;
		
		float size=0.7f;
    	float alpha=Optics.GetAlphaFromMagnitude(Mag, bglight);
		
		pos=Vec.Div(pos, pos.Size());
    	Vec difm=Vec.Cross(pos, new Vec(0.0,0.0,1.0));
    	difm=Vec.Mul(difm, size/difm.Size());
    	Vec difm2=Vec.Cross(difm, pos);
    	pos=Vec.Mul(pos, 99.0);
    
    	GL11.glColor4d(1.0, 1.0, 1.0, weathereff*alpha);
        
        tessellator1.startDrawingQuads();
    	tessellator1.addVertexWithUV(pos.x+difm.x, pos.y+difm.y, pos.z+difm.z,0.0,0.0);
    	tessellator1.addVertexWithUV(pos.x+difm2.x, pos.y+difm2.y, pos.z+difm2.z,1.0,0.0);
    	tessellator1.addVertexWithUV(pos.x-difm.x, pos.y-difm.y, pos.z-difm.z,1.0,1.0);
    	tessellator1.addVertexWithUV(pos.x-difm2.x, pos.y-difm2.y, pos.z-difm2.z,0.0,1.0);

        tessellator1.draw();
	}
}

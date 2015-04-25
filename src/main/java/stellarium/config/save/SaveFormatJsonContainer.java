package stellarium.config.save;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import sciapi.api.basis.SciAPI;
import stellarium.config.ICfgMessage;
import stellarium.config.StrMessage;
import stellarium.config.json.DummyJsonContainer;
import stellarium.config.json.IJsonContainer;
import stellarium.config.json.IJsonPropertyWriter;
import stellarium.config.json.JsonCfgTypeAdapterFactory;
import stellarium.config.json.JsonConfigHandler;
import stellarium.lang.CPropLangUtil;

public class SaveFormatJsonContainer extends DummyJsonContainer implements IJsonContainer {

	private static String CONFIGTAG = "c_config";
	private NBTTagCompound tag;
	
	private Map<String, SaveFormatJsonContainer> subContainers = Maps.newHashMap();
	
	public void bindNBT(NBTTagCompound tag) {
		this.tag = tag;
	}
	
	@Override
	public String getContextString() {
		return tag.getString(CONFIGTAG);
	}

	@Override
	public void setContextString(String context) {
		tag.setString(CONFIGTAG, context);
	}
	
	@Override
	public IJsonContainer makeSubContainer(String sub) {
		if(subContainers.containsKey(sub))
			return subContainers.get(sub);
		
		SaveFormatJsonContainer newContainer = new SaveFormatJsonContainer();
		newContainer.bindNBT(tag.getCompoundTag(sub));
		subContainers.put(sub, newContainer);
		return newContainer;
	}

	@Override
	public void removeSubContainer(String sub) {
		tag.removeTag(sub);
		subContainers.remove(sub);
	}

	@Override
	public IJsonContainer getSubContainer(String sub) {
		return subContainers.get(sub);
	}

	@Override
	public IJsonContainer moveSubContainer(String before, String after) {
		SaveFormatJsonContainer subContainer = subContainers.get(before);
		if(subContainer != null) {
			subContainers.remove(after);
			tag.removeTag(before);
			tag.setTag(after, subContainer.tag);
			subContainers.put(after, subContainer);
		}
		return subContainer;
	}

	@Override
	public List<String> getAllSubContainerNames() {
		return ImmutableList.copyOf(subContainers.keySet());
	}

	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		throw new LoadFailException(title, msg);
	}

}

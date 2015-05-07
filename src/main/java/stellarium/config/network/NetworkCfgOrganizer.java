package stellarium.config.network;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayerMP;
import stellarium.Stellarium;
import stellarium.config.IConfigCategory;
import stellarium.config.core.ConfigDataPhysicalManager;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.json.JsonConfigHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkCfgOrganizer {
	
	private SimpleNetworkWrapper network;
	private NetworkCfgNode baseNode;
	private Map<UUID, NetworkCfgNode> nodeMap = Maps.newHashMap();
	private int countNode, setCount;
	
	public NetworkCfgOrganizer(SimpleNetworkWrapper network) {
		this.network = network;
	}
	
	//Called when server about to start, before sending packet
	public void onSetupServer() {
		this.countNode = 0;
		baseNode = new NetworkCfgNode(this, "");
		
		for(StellarConfiguration config : ConfigDataPhysicalManager.getManager(Side.SERVER).getImmutableCfgList())
		{
			NetworkCfgNode node = addNode(baseNode, config.title);
			config.setHandler(new JsonConfigHandler(node));
			config.onFormat();
			config.onSave();
		}
	}
	
	//Called after receiving final configuration packet
	public void onSetupClient() {
		for(NetworkCfgNode node : baseNode.getSubNodes())
		{
			StellarConfiguration config = ConfigDataPhysicalManager.getManager(Side.CLIENT).getConfig(node.getTitle());
			config.setHandler(new JsonConfigHandler(node));
			config.onFormat();
			config.onApply();
		}
	}
	
	public NetworkCfgNode addNode(NetworkCfgNode parNode, String title) {
		if(parNode != null)
			this.countNode++;
		return this.addNode(parNode, UUID.randomUUID(), title);
	}
	
	public NetworkCfgNode addNode(NetworkCfgNode parNode, UUID id, String title) {
		NetworkCfgNode node = new NetworkCfgNode(parNode, id, title);
		parNode.getSubNodes().add(node);
		nodeMap.put(id, node);
		return node;
	}
	

	public void removeNode(NetworkCfgNode node) {
		nodeMap.remove(node.getID());
		if(node.hasParent())
			node.getParent().getSubNodes().remove(node);
	}
	
	public int getNodeCount() {
		return this.countNode;
	}
	
	public void setTotalCount(int setCount) {
		this.setCount = setCount;
	}
	
	public void sendConfigTo(EntityPlayerMP player) {
		this.sendConfigTo(this.baseNode, player);
	}

	private void sendConfigTo(NetworkCfgNode node, EntityPlayerMP player) {		
		for(NetworkCfgNode subNode : node.getSubNodes())
		{
			if(subNode.getSubNodes().isEmpty())
				network.sendTo(new ConfigInfoMessage(node.getID(), subNode.getID(), subNode), player);
			else network.sendTo(new ConfigEntryMessage(node.getID(), subNode.getID()), player);
			this.sendConfigTo(subNode, player);
		}
	}
	
	public NetworkCfgNode getNetNode(UUID thisId) {
		return nodeMap.get(thisId);
	}
	
	public void receiveConfig(UUID parentId, UUID thisId, String title) {
		if(!nodeMap.containsKey(parentId))
			baseNode = new NetworkCfgNode(this, parentId, "");
		
		this.addNode(nodeMap.get(parentId), thisId, title);
		
		if(this.countNode == this.setCount)
			this.onSetupClient();
	}

	public void receiveCfgInfo(UUID parentId, UUID thisId, String title, String context) {
		if(!nodeMap.containsKey(parentId))
			baseNode = new NetworkCfgNode(this, parentId, "");
		
		NetworkCfgNode node = this.addNode(nodeMap.get(parentId), thisId, title);
		node.setContextString(context);
		
		if(this.countNode == this.setCount)
			this.onSetupClient();
	}
	
}

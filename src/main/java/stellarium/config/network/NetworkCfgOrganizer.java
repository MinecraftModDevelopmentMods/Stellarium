package stellarium.config.network;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayerMP;
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
	
	public NetworkCfgOrganizer(SimpleNetworkWrapper network) {
		this.network = network;
	}
	
	//Called before sending packet
	public void onSetupServer() {
		baseNode = new NetworkCfgNode(this, "");
		
		for(StellarConfiguration config : ConfigDataPhysicalManager.getManager(Side.SERVER).getImmutableCfgList())
		{
			NetworkCfgNode node = addNode(baseNode, config.title);
			config.setHandler(new JsonConfigHandler(node));
			config.onFormat();
			config.onSave();
		}
	}
	
	//Called after receiving packet
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
	}

	public void receiveCfgInfo(UUID thisId, String context) {
		NetworkCfgNode node = nodeMap.get(thisId);
		node.setContextString(context);
	}
	
}

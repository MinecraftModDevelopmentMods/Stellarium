package stellarium.config.network;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import stellarium.config.ICfgMessage;
import stellarium.config.core.ConfigDataPhysicalManager;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.json.DummyJsonContainer;
import stellarium.config.json.IJsonContainer;
import stellarium.lang.CPropLangUtil;

public class NetworkCfgNode extends DummyJsonContainer implements IJsonContainer {
	
	private final NetworkCfgOrganizer organizer;
	private final NetworkCfgNode parNode;
	private final UUID id;
	private String title;
	private final List<NetworkCfgNode> nodes = Lists.newArrayList();
	private String context;
	
	private String loadFailMessage = null;
	private int loadFailCount = 0;
	
	public NetworkCfgNode(NetworkCfgOrganizer org, String title) {
		this.parNode = null;
		this.organizer = org;
		this.id = UUID.randomUUID();
		this.title = title;
	}
	
	public NetworkCfgNode(NetworkCfgOrganizer org, UUID id, String title) {
		this.parNode = null;
		this.organizer = org;
		this.id = id;
		this.title = title;
	}
	
	public NetworkCfgNode(NetworkCfgNode parNode, String title) {
		this.parNode = parNode;
		this.organizer = parNode.organizer;
		this.id = UUID.randomUUID();
		this.title = title;
	}
	
	public NetworkCfgNode(NetworkCfgNode parNode, UUID id, String title) {
		this.parNode = parNode;
		this.organizer = parNode.organizer;
		this.id = id;
		this.title = title;
	}
	
	public boolean hasParent() {
		return this.parNode != null;
	}

	public NetworkCfgNode getParent() {
		return this.parNode;
	}
	
	public List<NetworkCfgNode> getSubNodes() {
		return this.nodes;
	}
	
	public UUID getID() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public StellarConfiguration getConfig(Side side) {
		if(parNode == null)
			return null;
		
		StellarConfiguration parConfig = parNode.getConfig(side);
		
		if(parConfig == null)
			return ConfigDataPhysicalManager.getManager(side).getConfig(this.title);
		
		return parConfig.getSubConfigRaw(parConfig.getRootEntry().getChildEntry(this.title).getCategory());
	}

	
	@Override
	public IJsonContainer makeSubContainer(String sub) {
		return organizer.addNode(this, sub);
	}

	@Override
	public void removeSubContainer(String sub) {
		for(NetworkCfgNode node : nodes) {
			if(node.title.equals(sub))
			{
				organizer.removeNode(node);
				break;
			}
		}
	}

	@Override
	public IJsonContainer getSubContainer(String sub) {
		for(NetworkCfgNode node : nodes) {
			if(node.title.equals(sub))
				return node;
		}
		
		return null;
	}

	@Override
	public IJsonContainer moveSubContainer(String before, String after) {
		for(NetworkCfgNode node : nodes) {
			if(node.title.equals(before))
			{
				node.title = after;
				return node;
			}
		}
		
		return null;
	}

	@Override
	public List<String> getAllSubContainerNames() {
		List<String> names = Lists.newArrayList();
		
		for(NetworkCfgNode node : nodes) {
			names.add(node.title);
		}
		
		return names;
	}

	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		if(this.loadFailCount <= 0)
		{
			this.loadFailMessage = String.format("[%s]%s", CPropLangUtil.getLocalizedFromID(title),
					CPropLangUtil.getLocalizedMessage(msg));
			
			if(loadFailMessage.length() > 27)
				this.loadFailMessage = loadFailMessage.substring(0, 30) + "...";
		}
		this.loadFailCount++;
	}
	
	public boolean hasLoadFailMessage() {
		return this.loadFailCount > 0;
	}
	
	public String getLoadFailMessage() {
		return String.format("%s and %d More", this.loadFailMessage, this.loadFailCount);
	}

	@Override
	public String getContextString() {
		return context;
	}

	@Override
	public void setContextString(String context) {
		this.context = context;
	}
	
	
	@Override
	public boolean equals(Object o) {
		return (o != null) && (o instanceof NetworkCfgNode) && this.hashCode() == o.hashCode();
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}

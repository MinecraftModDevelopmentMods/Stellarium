package stellarium.config;

/**
 * Property Relation, relation between properties which can control their related behaviors.
 * Will be removed when any of its related property is removed
 * */
public interface IPropertyRelation {
	
	/**sets the properties of this relation.*/
	public void setProps(IMConfigProperty... props);

	/**called when enabling related property value.*/
	public void onEnable(int i);
	
	/**called when disabling related property value.*/
	public void onDisable(int i);
	
	/**called when property value is changed.*/
	public void onValueChange(int i);
	
	/**Tooltip String for Property Relation. Can be localized via CPropLangRegistry.*/
	public String getRelationToolTip();

}

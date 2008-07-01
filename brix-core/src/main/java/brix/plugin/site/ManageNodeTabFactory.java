package brix.plugin.site;

import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.IModel;

import brix.jcr.wrapper.BrixNode;
import brix.registry.ExtensionPoint;

/**
 * Factory for creating site node management tabs.
 * 
 * @author Matej Knopp
 */
public interface ManageNodeTabFactory
{

	public static final ExtensionPoint<ManageNodeTabFactory> POINT = new ExtensionPoint<ManageNodeTabFactory>()
	{
		public brix.registry.ExtensionPoint.Multiplicity getMultiplicity()
		{
			return Multiplicity.COLLECTION;
		}

		public String getUuid()
		{
			return ManageNodeTabFactory.class.getName();
		}
	};

	/**
	 * Returns this factory priority. The priority affects tab order. Factories
	 * with bigger priorities will have their tabs ordered before factories with
	 * lover priorities.
	 * 
	 * @return
	 */
	public int getPriority();

	/**
	 * Returns list of node management tabs for given node. 
	 * 
	 * @param nodeModel
	 * @return
	 */
	public List<ITab> getManageNodeTabs(IModel<BrixNode> nodeModel);

}

package brix.web.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.wicket.markup.html.tree.AbstractTree;

/**
 * AbstractTreeModel that delegates the tree structure to {@link TreeNode}. It
 * also contains convenience methods that inform the tree component that the
 * tree structure has been changed.
 * 
 * @author Matej Knopp
 */
public abstract class AbstractTreeModel implements Serializable, TreeModel
{
	/**
	 * Returns the root node if this tree. This is the only mandatory method to
	 * be implemented for custom tree models.
	 */
	public abstract TreeNode getRoot();

	private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>(0);

	public void removeTreeModelListener(TreeModelListener l)
	{
		listeners.remove(l);
	}

	public void addTreeModelListener(TreeModelListener l)
	{
		listeners.add(l);
	}

	public Object getChild(Object parent, int index)
	{
		List<?> children = ((TreeNode) parent).getChildren();
		return children != null ? children.get(index) : null;
	}

	public int getChildCount(Object parent)
	{
		List<?> children = ((TreeNode) parent).getChildren();
		return children != null ? children.size() : 0;
	}

	public int getIndexOfChild(Object parent, Object child)
	{
		List<?> children = ((TreeNode) parent).getChildren();
		return children != null ? children.indexOf(child) : -1;
	}

	public boolean isLeaf(Object node)
	{
		return ((TreeNode) node).isLeaf();
	}

	public void valueForPathChanged(TreePath path, Object newValue)
	{

	}

	public Object getParent(AbstractTree tree, Object node)
	{
		return tree.getParentNode(node);
	}

	private TreePath pathFromNode(AbstractTree tree, TreeNode node)
	{
		List<TreeNode> l = new ArrayList<TreeNode>();
		for (TreeNode n = node; n != null; n = (TreeNode) getParent(tree, n))
		{
			l.add(0, n);
		}
		return new TreePath(l.toArray(new TreeNode[l.size()]));
	}

	/**
	 * Notifies the tree that the given node has been changed while it's
	 * children remained unchanged.
	 * 
	 * @param tree
	 * @param node
	 */
	public void nodeChanged(AbstractTree tree, TreeNode node)
	{
		TreeNode parent = (TreeNode) getParent(tree, node);
		if (parent != null)
		{
			int index = parent.getChildren().indexOf(node);
			if (index != -1)
			{
				TreeModelEvent e = new TreeModelEvent(this, pathFromNode(tree, parent), new int[] { index },
						new Object[] { node });
				for (TreeModelListener l : listeners)
				{
					l.treeNodesChanged(e);
				}
			}
		}
	}

	/**
	 * Notifies the tree that the given node has been added.
	 * 
	 * @param tree
	 * @param node
	 */
	public void nodeInserted(AbstractTree tree, TreeNode parent, TreeNode node)
	{
		int index = parent.getChildren().indexOf(node);
		if (index != -1)
		{
			TreeModelEvent e = new TreeModelEvent(this, pathFromNode(tree, parent), new int[] { index },
					new Object[] { node });
			for (TreeModelListener l : listeners)
			{
				l.treeNodesInserted(e);
			}
		}

	}

	/**
	 * Notifies the tree that the given node will be removed. This method must
	 * be called <b>before</b> the node is actually deleted.
	 * 
	 * @param tree
	 * @param node
	 */
	public void nodeDeleted(AbstractTree tree, TreeNode node)
	{
		TreeNode parent = (TreeNode) getParent(tree, node);
		if (parent != null)
		{
			int index = parent.getChildren().indexOf(node);
			if (index != -1)
			{
				TreeModelEvent e = new TreeModelEvent(this, pathFromNode(tree, parent), new int[] { index },
						new Object[] { node });
				for (TreeModelListener l : listeners)
				{
					l.treeNodesRemoved(e);
				}
			}
		}
	}

	/**
	 * Notifies the tree that the children of given node have been changed.
	 * 
	 * @param tree
	 * @param node
	 */
	public void nodeChildrenChanged(AbstractTree tree, TreeNode node)
	{
		TreeModelEvent event = new TreeModelEvent(this, pathFromNode(tree, node));
		for (TreeModelListener l : listeners)
		{
			l.treeStructureChanged(event);
		}
	}
}

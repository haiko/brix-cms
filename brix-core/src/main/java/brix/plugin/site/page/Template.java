package brix.plugin.site.page;

import javax.jcr.Node;
import javax.jcr.Workspace;

import brix.Brix;
import brix.jcr.NodeWrapperFactory;
import brix.jcr.RepositoryUtil;
import brix.jcr.api.JcrNode;
import brix.jcr.api.JcrSession;
import brix.jcr.wrapper.BrixFileNode;
import brix.jcr.wrapper.BrixNode;

public class Template extends AbstractContainer
{
    public static NodeWrapperFactory FACTORY = new NodeWrapperFactory()
    {
        @Override
        public boolean canWrap(JcrNode node)
        {
            return TemplateSiteNodePlugin.TYPE.equals(getNodeType(node));
        }

        @Override
        public JcrNode wrap(JcrNode node)
        {
            return new Template(node, node.getSession());
        }

        @Override
        public void initializeRepository(Workspace workspace)
        {
            RepositoryUtil.registerMixinType(workspace, TemplateSiteNodePlugin.TYPE, false, false);
        }
    };


    public static final String CONTENT_TAG = Brix.NS_PREFIX + "content";

    public Template(Node delegate, JcrSession session)
    {
        super(delegate, session);
    }

    public static boolean canHandle(JcrNode node)
    {
        return TemplateSiteNodePlugin.TYPE.equals(getNodeType(node));
    }

    public static Template initialize(JcrNode node)
    {
        BrixNode brixNode = (BrixNode)node;
        BrixFileNode.initialize(node, "text/html");
        brixNode.setNodeType(TemplateSiteNodePlugin.TYPE);

        return new Template(node.getDelegate(), node.getSession());
    }
}

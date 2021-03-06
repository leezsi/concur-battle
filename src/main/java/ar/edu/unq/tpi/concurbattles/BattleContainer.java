package ar.edu.unq.tpi.concurbattles;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

import buoy.internal.WidgetContainerPanel;
import buoy.widget.Widget;
import buoy.widget.WidgetContainer;
import buoy.xml.WidgetEncoder;
import buoy.xml.delegate.IndexedContainerDelegate;


public class BattleContainer extends WidgetContainer
{
	
  private ArrayList<Widget> children;
  private ArrayList<Rectangle> childBounds;
  private Dimension requiredSize;
  
  static
  {
    WidgetEncoder.setPersistenceDelegate(BattleContainer.class, new IndexedContainerDelegate(new String [] {"getChild", "getChildBounds"}));
  }

  /**
   * Create a new ExplicitContainer.
   */
  
  public BattleContainer()
  {
    component = new WidgetContainerPanel(this);
    children = new ArrayList<Widget>();
    childBounds = new ArrayList<Rectangle>();
    requiredSize = new Dimension(0, 0);
  }

  public JPanel getComponent()
  {
    return (JPanel) component;
  }

  /**
   * Get the number of children in this container.
   */
  
  public int getChildCount()
  {
    return children.size();
  }
  
  /**
   * Get the i'th child of this container.
   */
  
  public Widget getChild(int i)
  {
    return children.get(i);
  }
  
  /**
   * Get a Collection containing all child Widgets of this container.
   */
  
  public Collection<Widget> getChildren()
  {
    return new ArrayList<Widget>(children);
  }
  
  /**
   * Layout the child Widgets.  This may be invoked whenever something has changed (the size of this
   * WidgetContainer, the preferred size of one of its children, etc.) that causes the layout to no
   * longer be correct.  If a child is itself a WidgetContainer, its layoutChildren() method will be
   * called in turn.
   */
  
  public void layoutChildren()
  {
    for (int i = 0; i < children.size(); i++)
    {
      Widget child = children.get(i);
      Rectangle bounds = childBounds.get(i);
      child.getComponent().setBounds(bounds);
      if (child instanceof WidgetContainer)
        ((WidgetContainer) child).layoutChildren();
    }
  }
  
  /**
   * Add a Widget to this container.
   *
   * @param widget     the Widget to add
   * @param bounds     the location and size at which the Widget should appear
   */
  
  public void add(Widget widget, Rectangle bounds)
  {
    if (widget.getParent() != null)
      widget.getParent().remove(widget);
    children.add(widget);
    childBounds.add(new Rectangle(bounds));
    getComponent().add(widget.getComponent());
    widget.getComponent().setBounds(bounds);
    setAsParent(widget);
    requiredSize.width = Math.max(requiredSize.width, bounds.x+bounds.width);
    requiredSize.height = Math.max(requiredSize.height, bounds.y+bounds.height);
    invalidateSize();
  }

  /**
   * Get the index of a particular Widget.
   *
   * @param widget      the Widget to locate
   * @return the index of the Widget within this container
   */
  
  public int getChildIndex(Widget widget)
  {
    return children.indexOf(widget);
  }
  
  /**
   * Get the position of a child Widget within this container.
   *
   * @param index     the index of the Widget for which to get the location
   * @return the bounding rectangle to be used for the Widget
   */
  
  public Rectangle getChildBounds(int index)
  {
    return new Rectangle(childBounds.get(index));
  }
  
  /**
   * Set the position of a child Widget within this container.
   *
   * @param index      the index of the Widget to move
   * @param bounds     the location and size at which the Widget should appear
   */
  
  public void setChildBounds(int index, Rectangle bounds)
  {
    childBounds.set(index, new Rectangle(bounds));
    findRequiredSize();
  }
  
  /**
   * Get the position of a child Widget within this container.
   *
   * @param widget     the Widget for which to get the location
   * @return the bounding rectangle to be used for the Widget, or null if it is not a child
   * of this container
   */
  
  public Rectangle getChildBounds(Widget widget)
  {
    int index = children.indexOf(widget);
    if (index > -1)
      return new Rectangle(childBounds.get(index));
    return null;
  }
  
  /**
   * Set the position of a child Widget within this container.
   *
   * @param widget     the Widget to move
   * @param bounds     the location and size at which the Widget should appear
   */
  
  public void setChildBounds(Widget widget, Rectangle bounds)
  {
    int index = children.indexOf(widget);
    if (index > -1)
    {
      childBounds.set(index, new Rectangle(bounds));
      findRequiredSize();
    }
  }
  
  /**
   * Remove a child Widget from this container.
   *
   * @param widget     the Widget to remove
   */
  
  public void remove(Widget widget)
  {
    int index = children.indexOf(widget);
    if (index > -1)
    {
      getComponent().remove(widget.getComponent());
      children.remove(index);
      childBounds.remove(index);
      removeAsParent(widget);
      findRequiredSize();
    }
  }
  
  /**
   * Remove all child Widgets from this container.
   */
  
  public void removeAll()
  {
    getComponent().removeAll();
    for (int i = 0; i < children.size(); i++)
      removeAsParent((Widget) children.get(i));
    children.clear();
    childBounds.clear();
    requiredSize.width = requiredSize.height = 0;
    invalidateSize();
  }
  
  /**
   * Calculate how large this Widget needs to be to hold all of its children.
   */
  
  private void findRequiredSize()
  {
    requiredSize.width = requiredSize.height = 0;
    for (int i = childBounds.size()-1; i >= 0; i--)
    {
      Rectangle r = childBounds.get(i);
      requiredSize.width = Math.max(requiredSize.width, r.x+r.width);
      requiredSize.height = Math.max(requiredSize.height, r.y+r.height);
    }
    invalidateSize();
  }
  
  /**
   * Get the smallest size at which this Widget can reasonably be drawn.  When a WidgetContainer lays out
   * its contents, it will attempt never to make this Widget smaller than its minimum size.
   */
  
  public Dimension getMinimumSize()
  {
    return requiredSize;
  }

  /**
   * Get the preferred size at which this Widget will look best.  When a WidgetContainer lays out
   * its contents, it will attempt to make this Widget as close as possible to its preferred size.
   */
  
  public Dimension getPreferredSize()
  {
    return requiredSize;
  }
}
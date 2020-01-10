package util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class that organizes its data in ascending order
 */
public class LED implements Iterable<Node>{

    private ArrayList<Node> nodes = new ArrayList<>();
    private static final int MINIMUM_TIME_SPACE = 99; //98ms in spacing between each node(Each node has it's own independent space of 196ms) Selecting a node will depend on 50s of space given
    private final int BINARARY_SEARCH_TIME_SPACE =MINIMUM_TIME_SPACE/2 +2; // Exists since the insertion number is 99 to make 100 a clean spacing, this filler respects the time space, and
    private int core;
    private int ID;
    private boolean isAnalog;

    /**
     *
     * @param ID
     * @param core
     * @param isAnalog
     */
    public LED(int ID, int core, boolean isAnalog)
    {
        this.ID = ID;
        this.core = core;
        this.isAnalog =isAnalog;
    }

    /**
     * A fast insertion method to permit a node to be added with O(log n)
     * A node will not be added if it overlaps within the range of 100ms of another node
     * Pivots more specific to properly insert to the left or right of the (Low&&Max)
     * @param toAdd node to be added
     * @param low range pivot
     * @param high range pivot
     */
    private void binaryInsertion(Node toAdd, int low, int high)
    {
        if(low >= high)
        {
            checkConflict(toAdd, low);
            if(low > 0)
            {
                checkConflict(toAdd, low-1);
            }
            if(nodes.get(low).getStartTime() > toAdd.getEndTime())
            {
                nodes.add(low, toAdd);//replaces low, and low becomes low +1
                return;
            }
            else
            {
                if(low < nodes.size()-2)
                {
                    checkConflict(toAdd, low+1);
                }
                else
                {
                    nodes.add(toAdd);//adds to end of index
                    return;
                }
                nodes.add(low+1, toAdd);//adds in front of low (pushing low +1 to low +2)
                return;
            }
        }

        int mid = (low + high)/2;
        checkConflict(toAdd, mid);
        if(mid+1 < nodes.size()) {
            checkConflict(toAdd, mid + 1);
        }

        if(nodes.get(mid).getStartTime() - MINIMUM_TIME_SPACE > toAdd.getEndTime())
        {
            binaryInsertion(toAdd, low, mid);
        }
        else
        {
            binaryInsertion(toAdd,mid+1, high);
        }
    }

    /**
     * Adds a node to an empty list, or inserts it between nodes in ascending order of time
     * @param toAdd node to be added
     */
    public void addNode(Node toAdd)
    {
        //Empty list simply adds the node.
        if(nodes.isEmpty())
        {
            nodes.add(toAdd);
            return;
        }
        int i = 0;
        checkConflict(toAdd, i); // throws an exception and modifies ui to show nothing happened.
        binaryInsertion(toAdd, 0, nodes.size()-1);
    }

    /**
     * Throws an exception for any nodes that overlap(within the range of 100ms) any nodes before, on, or after the current, thus preventing a created node from being inserted if it does not respect the range.
     * @param toAdd node to be added
     * @param i index to compare
     */
    private void checkConflict(Node toAdd, int i) {
        if(((nodes.get(i).getStartTime()-MINIMUM_TIME_SPACE < toAdd.getStartTime()) && (nodes.get(i).getEndTime() + MINIMUM_TIME_SPACE  > toAdd.getStartTime())) || ((nodes.get(i).getStartTime()-MINIMUM_TIME_SPACE < toAdd.getEndTime()) && (nodes.get(i).getEndTime() + MINIMUM_TIME_SPACE  > toAdd.getEndTime())))
        {
            System.out.println("exceppppt");
            //throw an exception to prevent adding this node, GUI will modify accordingly to notify
        }
    }

    private Node binarySearch(int time, int low, int high)
    {
        if(nodes.isEmpty())
        {
            return null;
        }
        if(nodes.size() == 1)
        {
            if((nodes.get(0).getStartTime() - BINARARY_SEARCH_TIME_SPACE < time)  && (nodes.get(0).getEndTime() + BINARARY_SEARCH_TIME_SPACE > time))
            {
                return nodes.get(0);
            }
            return null;//Practically else
        }
        //Since every node with a time value is unique through a range of
        if(low == high)
        {
            if((nodes.get(low).getStartTime() - BINARARY_SEARCH_TIME_SPACE < time)  && (nodes.get(low).getEndTime() + BINARARY_SEARCH_TIME_SPACE > time))
            {
                return nodes.get(low);
            }
            return null;//Practically else
        }
        int mid = (low + high)/2;
        if((nodes.get(mid).getStartTime() - BINARARY_SEARCH_TIME_SPACE < time)  && (nodes.get(mid).getEndTime() + BINARARY_SEARCH_TIME_SPACE > time))
        {
            return nodes.get(mid);
        }
        else if((nodes.get(mid).getStartTime() - BINARARY_SEARCH_TIME_SPACE > time))
        {
            return binarySearch(time, low, mid);
        }
        else if((nodes.get(mid).getEndTime() + BINARARY_SEARCH_TIME_SPACE < time))
        {
            return binarySearch(time, mid+1, high);
        }
        return null;
    }

    public Node getSelection(int time)
    {
        //Named for readability for UI calls
        return binarySearch(time, 0, nodes.size()-1);//will return null, if UI reads NULL, an exception my be thrown to modify UI in lower functions, OR simply use == null conditional to modify
    }

    /**
     * create either a bar node or a digital node (Conditionals developed for GUI use)
     * @param time time point to create the node at
     * @param intensity the initial data value to consider when creating the node
     * @param isBar checks if it should create a bar or point node
     */
    public void createNode(int time, int intensity, boolean isBar)
    {
        //Note: create node covers cases that a bar node can be made while an LED is digital
        Node toAdd;
        if(!isAnalog)
        {
            if(intensity > 126)
            {
                if(isBar)
                {
                    toAdd = new BarNode(time, 255, this);
                }
                else {
                    toAdd = new PointNode(time, 255, this);
                }
            }
            else
            {
                if(isBar) {
                    toAdd = new BarNode(time, 0, this);
                }
                else
                {
                    toAdd = new PointNode(time, 0, this);
                }
            }
        }
        else
        {
            if(intensity > 255)
            {
                if(isBar) {
                    toAdd = new BarNode(time, 255, this);
                }
                else
                {
                    toAdd = new PointNode(time, 255, this);
                }
            }
            else if(intensity < 0)
            {
                if(isBar) {
                    toAdd = new BarNode(time, 0, this);
                }
                else
                {
                    toAdd = new PointNode(time, 0, this);
                }
            }
            else
            {
                if(isBar)
                {
                    toAdd = new BarNode(time, intensity, this);
                }
                else
                {
                    toAdd = new PointNode(time, intensity, this);
                }
            }
        }
        addNode(toAdd);
    }
    /**
     * Returns a specific string for an arduino to easily parse VIA serial or json file. This toString will output the identification info for this LED
     * @return String
     */
    @Override
    public String toString()
    {
        return "";//To be determined(Need to find a diagram :/ )
    }

    /**
     * Makes the LED object iterable, to easily extract it's data with custom iteration extraction (for graphing)
     * @return nodes arraylist iterator
     */
    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}

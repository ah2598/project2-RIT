package model;

import java.util.ArrayList;

/**
 * Represents a Quadtree node in the tree for an image compressed using the
 * Rich Image Tool file format.
 *
 * A node contains a value which is either a grayscale color (0-255) for a
 * region, or QTree.QUAD_SPLIT meaning this node cannot hold a single color
 * and thus has split itself into 4 sub-regions.
 *
 * @author Sean Strout @ RIT
 */
public class RITQTNode {
    /** Value if this node is an interior node*/
    private static final int SPLIT_VALUE = -1;

    /** The node's value */
    private int val;

    /** quadrant II */
    private RITQTNode ul;

    /** quadrant I */
    private RITQTNode ur;

    /** quadrant III */
    private RITQTNode ll;

    /** quadrant IV */
    private RITQTNode lr;

    /**
     * Construct a leaf node with no children.
     * @param val node value
     */
    public RITQTNode(int val) {
        this(val, null, null, null, null);
    }

    /**
     * Construct a quad tree node.
     *
     * @param val the node's value
     * @param ul the upper left sub-node
     * @param ur the upper right sub-node
     * @param ll the lower left sub-node
     * @param lr the lower right sub-node
     */
    public RITQTNode(int val, RITQTNode ul, RITQTNode ur, RITQTNode ll, RITQTNode lr) {
        this.val = val;
        this.ul = ul;
        this.ur = ur;
        this.ll = ll;
        this.lr = lr;
    }

    /**
     * Get the node's value.
     *
     * @return node's value
     */
    public int getVal() { return this.val; }

    /**
     * Get the upper left sub-node.
     *
     * @return upper left sub-node
     */
    public RITQTNode getUpperLeft() { return this.ul; }

    /**
     * Get the upper right sub-node.
     *
     * @return upper right sub-node
     */
    public RITQTNode getUpperRight() { return this.ur; }

    /**
     * Get the lower left sub-node.
     *
     * @return lower left sub-node
     */
    public RITQTNode getLowerLeft() { return this.ll; }

    /**
     * Get the lower right sub-node
     *
     * @return lower right sub-node
     */
    public RITQTNode getLowerRight() { return this.lr; }

    /**
     * Assuming this quadtree is the root,
     * this quadtree is converted to a 2D image representation
     *
     * @param image the 2D image array to modify
     * @param size the side length of this quadtree
     * @return arraylist containing this image's pixel values
     */
    public int[][] uncompress(int[][] image, int size)
    {
        return uncompress(image, 0, 0, size);
    }

    /**
     * Converts this quadtree into a 2D image representation,
     * rowStart/colStart/size values are used to determine this quadtree's position
     *
     * @param rowStart the row component of this quadtree's starting coordinates
     * @param colStart the col component of this quadtree's starting coordinates
     * @param size the size of this quadtree
     */
    private int[][] uncompress(int[][] image, int rowStart, int colStart, int size)
    {
        //Base case: Quadtree is a leaf node
        if(ul == null || ur == null || ll == null || lr == null)
        {
            for(int row = rowStart; row < rowStart + size; row++)
            {
                for(int col = colStart; col < colStart + size; col++)
                {
                    image[row][col] = val;
                }
            }
        }
        //Recursive case: Quadtree has children
        else
        {
            int childSize = (int)Math.sqrt(size);
            ul.uncompress(image, rowStart, colStart, childSize);
            ur.uncompress(image, rowStart, colStart + size/2, childSize);
            ll.uncompress(image, rowStart + size/2, colStart, childSize);
            lr.uncompress(image, rowStart + size/2, colStart + size/2, childSize);
        }

        return image;
    }

    @Override
    public String toString()
    {
        String string = String.valueOf(this.val);

        //Base case: Not an interior node
        if(val != -1)
        {
            return string + " ";
        }
        //Recursive case: An interior node
        else
        {
            return string + " " + ul.toString() + ur.toString() + ll.toString() + lr.toString();
        }
    }
}
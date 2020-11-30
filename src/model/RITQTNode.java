package model;

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
     * @param sideLength the sidelength  of this quadtree
     * @return a 2D image array representation of this quadtree
     */
    private int[][] uncompress(int[][] image, int rowStart, int colStart, int sideLength)
    {
        //Base case: Quadtree is a leaf node
        if(ul == null || ur == null || ll == null || lr == null)
        {
            for(int row = rowStart; row < rowStart + sideLength; row++)
            {
                for(int col = colStart; col < colStart + sideLength; col++)
                {
                    image[row][col] = val;
                }
            }
        }
        //Recursive case: Quadtree has children
        else
        {
            int childLength = sideLength/2;
            ul.uncompress(image, rowStart, colStart, childLength);
            ur.uncompress(image, rowStart, colStart + childLength, childLength);
            ll.uncompress(image, rowStart + childLength, colStart, childLength);
            lr.uncompress(image, rowStart + childLength, colStart + childLength, childLength);
        }

        return image;
    }

    /**
     * Assuming this quadtree is the root,
     * this 2D image array is converted to a quadtreee representation
     *
     * @param image the 2D image array to modify
     * @param size the side length of this quadtree
     * @return the quadtree representation of this image
     */
    public static RITQTNode compress(int[][] image, int size)
    {
        return compress(image, 0, 0, size);
    }

    /**
     * Converts the 2D image array into a quadtree
     * rowStart/colStart/size values are used to determine this quadtree's position
     *
     * @param rowStart the row component of this quadtree's starting coordinates
     * @param colStart the col component of this quadtree's starting coordinates
     * @param sideLength the side length of this quadtree
     * @return a quadtree representation of the 2D image array
     */
    public static RITQTNode compress(int[][] image, int rowStart, int colStart, int sideLength)
    {
        int childLength = sideLength/2;

        //Base case: This quadtree has sideLength = 1
        if(sideLength == 1)
            return new RITQTNode(image[rowStart][colStart]);

        //Recursive case: Processing of children nodes required
        RITQTNode ul = compress(image, rowStart, colStart, childLength);
        RITQTNode ur = compress(image, rowStart, colStart + childLength, childLength);
        RITQTNode ll = compress(image, rowStart + childLength, colStart, childLength);
        RITQTNode lr = compress(image, rowStart + childLength, colStart + sideLength/2, childLength);

        //Case 1: The four subsections are the same, combine children nodes
        if(ul.getVal() == ur.getVal() && ur.getVal() == ll.getVal() && ll.getVal() == lr.getVal())
            return new RITQTNode(ul.getVal());

        //Case 2: The four subsections aren't the same, return parent node
        else
            return new RITQTNode(-1, ul, ur, ll, lr);
    }

    @Override
    public String toString()
    {
        //Base case: Not an interior node
        if(val != -1)
        {
            return val + " ";
        }
        //Recursive case: An interior node
        else
        {
            return val + " " + ul.toString() + ur.toString() + ll.toString() + lr.toString();
        }
    }
}
package model.huffman.tree;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Huffman tree node. 
 * @author michael
 */
public class HuffmanNode extends TreeBase implements Comparable<HuffmanNode>{
	private static final Logger log=Logger.getLogger("HuffmanNode");
	
    public int codeword=-1;
    public int vertical=-1;
    public int horizontal=-1;
    public HuffmanNode zero_left=null;
    public HuffmanNode one_right=null;
    
    /**
     * Constructor.
     */
    public HuffmanNode() {
    	super(log);
    	
        this.codeword = -1;
        this.vertical=-1;    
        this.horizontal=-1;
        this.zero_left  = null;
        this.one_right =  null;
    }    
    
    /**
     * Constructor.
     * @param codeword
     * @param bits
     * @param zero_left
     * @param one_right
     */
    public HuffmanNode(int codeword, int vertical, int horizontal, HuffmanNode zero_left, HuffmanNode one_right) {
       	super(log);
       	
        this.codeword = codeword;
        this.vertical=vertical;        
        this.horizontal=horizontal;
        this.zero_left  = zero_left;
        this.one_right = one_right;
    }

    /**
     * is the node a leaf node?
     * @return
     */
    public boolean isLeaf() {
    	if (this.codeword==-1)
    		return false;
    	else
    		return true;
    }    
    
    /**
     * Compare function.
     */
	@Override
    // compare, based on frequency
    public int compareTo(HuffmanNode that) {
        return this.codeword - that.codeword;
    }
	
	/**
	 * Get binary sequence.
	 * @param inputTree
	 * @return
	 */
	public String getBinSequence(HuffmanTree inputTree){
		String returnBitSequence="";
		
		HuffmanNode currentNode=this;
				
		int newVert=this.vertical-1;
		for (int i=newVert;i>=0;i--){
			int newHor=inputTree.myHuffmanNodeTree[i].length;
			for (int j=0;j<newHor;j++){
				if (inputTree.myHuffmanNodeTree[i][j]!=null){
					if (inputTree.myHuffmanNodeTree[i][j].zero_left==currentNode){
						returnBitSequence="0"+returnBitSequence;
						currentNode=inputTree.myHuffmanNodeTree[i][j];
						break;
					}else if (inputTree.myHuffmanNodeTree[i][j].one_right==currentNode){
						returnBitSequence="1"+returnBitSequence;
						currentNode=inputTree.myHuffmanNodeTree[i][j];
						break;
					}
				}
			}
		}
		
		// And we need the last bit from the root
		if (inputTree.root.one_right!=null &&
			inputTree.root.one_right==currentNode){
			returnBitSequence="1"+returnBitSequence;
		}else if (inputTree.root.zero_left!=null &&
				inputTree.root.zero_left==currentNode){
			returnBitSequence="0"+returnBitSequence;
		}else{
			log.log(Level.SEVERE, "A: ERROR ERROR FIX ME!");
		}
		
		log.log(Level.FINEST, "B: Search for="+this.toString()+" bitSequence="+returnBitSequence);
				
		return returnBitSequence;
	}
	
	/**
	 * to String method.
	 */
	@Override
	public String toString(){
		String returnString="";
				
		if (this.isLeaf()==true){
			returnString+=Integer.toHexString(this.codeword);
		}
		
		return returnString;
	}
}
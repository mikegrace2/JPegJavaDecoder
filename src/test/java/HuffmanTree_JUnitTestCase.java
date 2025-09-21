import static org.junit.Assert.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.HuffmanSearchString;
import model.JPegData;
import model.huffman.tree.HuffmanTree;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.Exceptions.CannotFindHuffmanSearchString;
import control.modules.JPegFileReader;
import control.modules.ModulesBase;

/**
 * Test case for the Huffman trees which are included in a standard Jpeg file.
 * @author michael
 */
public class HuffmanTree_JUnitTestCase extends ModulesBase{
	private static final Logger log = Logger.getLogger("HuffmanTree_TestCase");
	
	private JPegData jPegData=null;

	public HuffmanTree_JUnitTestCase() {
		super(log);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Load the standard tables from a JPeg file. It doesn't matter which it is every JPeg contains it.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.jPegData=new JPegFileReader().readJPeg(
				"C:\\Users\\michael\\OneDrive\\SantaClaraUniversity\\2015-10.Winter\\COEN338_Multimedia Data Compression I Image and Video\\COEN338Project\\MySample_jpeg\\16times16Black_4_2_0.jpg");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * No gurantee that an jpeg contains 4 standard Huffmann tables.
	 */
	@Test	
	public void test_HuffmanTree_DCTTable0() {
		try {
			// Print Tree
			// ----------------------------------------------------------------------------------
			log.log(Level.FINEST, "\n=== Print Tree ===");
			// mySimpleConvertImage.myJPegData.huffmantables[0].dhtHuffmanTree.printHuffmannTree();

			// Which table is next
			int huffmanTablesIntPointer = 0;

			log.log(Level.INFO, "");

			// Check standard Huffman DCT table 0
			// ---------------------------------------------------------------------------------
			if (this.jPegData.getAvailableHuffmanTablesAsIntArray().length >= 1) {
				log.log(Level.INFO, "1: DCT table 0 ---");
				int myIntArr0[][] = new int[][] {
						{ 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 0, 1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 0, 1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 0,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 1,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 1,  1,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 1,  1,  1,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 1,  1,  1,  1,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 1,  1,  1,  1,  1,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 1,  1,  1,  1,  1,  1,  1,  0, -1, -1, -1, -1, -1, -1, -1 } };

				int nextHuffmanTabble = this.jPegData
						.getAvailableHuffmanTablesAsIntArray()[huffmanTablesIntPointer];
				huffmanTablesIntPointer = huffmanTablesIntPointer + 1;

				for (int i = 0; i < myIntArr0.length; i++) {
					int codeWord = this.jPegData.huffmantables[nextHuffmanTabble].dhtHuffmanTree
							.getCodeWord(new HuffmanSearchString(myIntArr0[i]));
					log.log(Level.INFO, "2: Search for entry ="
							+ java.util.Arrays.toString(myIntArr0[i])
							+ "codeword=" + Integer.toHexString(codeWord));
				}
				log.log(Level.INFO, "");
			}
		} catch (CannotFindHuffmanSearchString ex) {
			log.log(Level.SEVERE, "3: CannotFindHuffmanSearchString EXCEPTION!");
			fail("CannotFindHuffmanSearchString Exception!");
		}
	}
	
	/**
	 * No guarantee that an jpeg contains 4 standard Huffmann tables.
	 */
	@Test	
	public void test_HuffmanTree_DCTTable1() {
		try {
			// Print Tree
			// ----------------------------------------------------------------------------------
			log.log(Level.FINEST, "\n=== Print Tree ===");
			// mySimpleConvertImage.myJPegData.huffmantables[0].dhtHuffmanTree.printHuffmannTree();

			// Which table is next
			int huffmanTablesIntPointer = 0;

			log.log(Level.INFO, "");

			// Check standard Huffman DCT table 1
			// ---------------------------------------------------------------------------------
			if (this.jPegData.getAvailableHuffmanTablesAsIntArray().length >= 2) {
				log.log(Level.INFO, "1: DCT table 1 ---");
				int myIntArr1[][] = new int[][] {
						{ 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 0, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1 },
						{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1 },
						{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1 } };

				int nextHuffmanTabble = this.jPegData
						.getAvailableHuffmanTablesAsIntArray()[huffmanTablesIntPointer];
				huffmanTablesIntPointer = huffmanTablesIntPointer + 1;

				for (int i = 0; i < myIntArr1.length; i++) {
					int codeWord = this.jPegData.huffmantables[nextHuffmanTabble].dhtHuffmanTree
							.getCodeWord(new HuffmanSearchString(myIntArr1[i]));
					log.log(Level.INFO, "4: Search for entry ="
							+ java.util.Arrays.toString(myIntArr1[i])
							+ "codeword=" + Integer.toHexString(codeWord));
				}
				log.log(Level.INFO, "");
			}
		} catch (CannotFindHuffmanSearchString ex) {
			log.log(Level.SEVERE, "2: CannotFindHuffmanSearchString EXCEPTION!");
			fail("CannotFindHuffmanSearchString Exception!");
		}
	}

	/**
	 * No gurantee that an jpeg contains 4 standard Huffmann tables.
	 */
	@Test	
	public void test_HuffmanTree_DCTTable16() {
		try {
			// Print Tree
			// ----------------------------------------------------------------------------------
			log.log(Level.FINEST, "\n=== Print Tree ===");
			// mySimpleConvertImage.myJPegData.huffmantables[0].dhtHuffmanTree.printHuffmannTree();

			// Which table is next
			int huffmanTablesIntPointer = 0;

			log.log(Level.INFO, "1");

			// Check standard Huffman DCT table 16
			// ---------------------------------------------------------------------------------
			if (this.jPegData.getAvailableHuffmanTablesAsIntArray().length >= 3) {
				log.log(Level.INFO, "2: ACT table 16 ---");
				int myIntArr16[][] = new int[][] {
						{ 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 0, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 0, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 0, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 0, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 0, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 1, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 1, 0, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 1, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 1, 0, 0, 1, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 1, 0, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 } };

				int nextHuffmanTabble = this.jPegData
						.getAvailableHuffmanTablesAsIntArray()[huffmanTablesIntPointer];
				huffmanTablesIntPointer = huffmanTablesIntPointer + 1;
				log.log(Level.INFO, "3: nextHuffmanTabble="
						+ nextHuffmanTabble);
				log.log(Level.INFO, "4: huffmanTablesIntPointer="
						+ huffmanTablesIntPointer);

				for (int i = 0; i < myIntArr16.length; i++) {
					HuffmanTree myHuffmanTree = this.jPegData.huffmantables[nextHuffmanTabble].dhtHuffmanTree;
					int codeWord = myHuffmanTree
							.getCodeWord(new HuffmanSearchString(myIntArr16[i]));
					log.log(Level.INFO, "5: Search for entry ="
							+ java.util.Arrays.toString(myIntArr16[i])
							+ "codeword=" + Integer.toHexString(codeWord));
				}
				log.log(Level.INFO, "");
			}
		} catch (CannotFindHuffmanSearchString ex) {
			log.log(Level.SEVERE, "6: CannotFindHuffmanSearchString EXCEPTION!");
			fail("CannotFindHuffmanSearchString Exception!");
		}
	}

	/**
	 * No gurantee that an jpeg contains 4 standard Huffmann tables.
	 */
	@Test	
	public void test_HuffmanTree_DCTTable17() {
		try {
			// Print Tree
			// ----------------------------------------------------------------------------------
			log.log(Level.FINEST, "\n=== Print Tree ===");
			// mySimpleConvertImage.myJPegData.huffmantables[0].dhtHuffmanTree.printHuffmannTree();

			// Which table is next
			int huffmanTablesIntPointer = 0;

			log.log(Level.INFO, "1");

			// Check standard Huffman DCT table 17
			// ---------------------------------------------------------------------------------
			if (this.jPegData.getAvailableHuffmanTablesAsIntArray().length >= 4) {
				log.log(Level.INFO, "2: ACT table 17 ---");
				int myIntArr17[][] = new int[][] {
						{ 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 0, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 0, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 0, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 0, 0, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 0, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 0, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1 },
						{ 1, 1, 1, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 0, 0, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 0, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 0, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1 },
						{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
						{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 } };

				int nextHuffmanTabble = this.jPegData
						.getAvailableHuffmanTablesAsIntArray()[huffmanTablesIntPointer];
				huffmanTablesIntPointer = huffmanTablesIntPointer + 1;

				for (int i = 0; i < myIntArr17.length; i++) {
					int codeWord = this.jPegData.huffmantables[nextHuffmanTabble].dhtHuffmanTree
							.getCodeWord(new HuffmanSearchString(myIntArr17[i]));
					log.log(Level.INFO, "3: Search for entry ="
							+ java.util.Arrays.toString(myIntArr17[i])
							+ "codeword=" + Integer.toHexString(codeWord));
				}
			}
		} catch (CannotFindHuffmanSearchString ex) {
			log.log(Level.SEVERE, "4: CannotFindHuffmanSearchString EXCEPTION!");
			fail("CannotFindHuffmanSearchString Exception!");
		}
	}
}
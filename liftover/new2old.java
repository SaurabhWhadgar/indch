import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.Object;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileWriter;

//class outlining the data of blocks of UCI corresponding with blocks of IndCh in bed6 format
class Data
{
	private int UCIstart;
	private int UCIstop;
	private int IndChstart;
	private int IndChstop;
	private String UCIchr;
	private String IndChchr;
	private String strand;

	// constructor
	public Data(int UCIstart, int UCIstop, int IndChstart, int IndChstop, String UCIchr, String IndChchr, String strand)
	{
		this.UCIstart = UCIstart;
		this.UCIstop = UCIstop;
		this.IndChstart = IndChstart;
		this.IndChstop = IndChstop;
		this.UCIchr = UCIchr;
		this.IndChchr = IndChchr;
		this.strand = strand;
	}

	// getter
	public int getUCIstart() { return UCIstart; }
	public int getUCIstop() { return UCIstop; }
	public int getIndChstart() { return IndChstart; }
	public int getIndChstop() { return IndChstop; }
	public String getUCIchr() { return UCIchr; }
	public String getIndChchr() { return IndChchr; }
	public String getStrand() { return strand; }
}

//class outlining details of query gff file
class Query
{
	private String chr;
	private String program;
	private String geneFeature;
	private int start;
	private int stop;
	private String score;
	private String strand;
	private String frame;
	private String comment;

	// constructor for gff format
	public Query(String chr, String program, String geneFeature, int start, int stop, String score, String strand, String frame, String comment)
	{
		this.chr=chr;
		this.program=program;
		this.geneFeature=geneFeature;
		this.start=start;
		this.stop=stop;
		this.score=score;
		this.strand=strand;
		this.frame=frame;
		this.comment=comment;
	}
	// constructor for bed6 format
	public Query(String chr, int start, int stop, String comment, String score, String strand)
	{
		this.chr = chr;
		this.start = start;
		this.stop = stop;
		this.comment = comment;
		this.score = score;
		this.strand = strand;
	}

	// getter
	public String getChr() { return chr; }
	public String getProgram() { return program; }
	public String getGeneFeature() { return geneFeature; }
	public int getStart() { return start; }
	public int getStop() { return stop; }
	public String getScore() { return score; }
	public String getStrand() { return strand; }
	public String getFrame() { return frame; }
	public String getComment() { return comment; }
}

 
public class new2old
{
	public String nucmer2customBED(String NucmerFile) throws Exception
	{
		//Reading file containing info on chr alignments, i.e., blocks in bed6 format
		System.out.println("Loading alignment . . . ");
		BufferedReader blocksFile = new BufferedReader(new FileReader(NucmerFile));
		String dataRow = blocksFile.readLine(); // Read first line.
		//Reading file till we reach the alignment data and ignoring the header lines
		//Making the output file
		String outfilebed = NucmerFile + ".bed";
		FileWriter out = new FileWriter(outfilebed);
		ArrayList<String> st_filtered = new ArrayList<String>();
		//Reading the alignment info
		while (dataRow != null)
		{
			//For filtering out the "|" and making bed file
			String[] st = dataRow.split("\t",-1);
			for(int elem=0; elem < st.length; elem++)
			{
				if(!st[elem].equals("|"))
				{
					st_filtered.add(st[elem]);
				}
			}
			int UCIstart = Integer.parseInt(st_filtered.get(0));
			int UCIstop = Integer.parseInt(st_filtered.get(1));
			int IndChstart = Integer.parseInt(st_filtered.get(2));
			int IndChstop = Integer.parseInt(st_filtered.get(3));
			String UCIchr = st_filtered.get(7);
			String IndChchr = st_filtered.get(8);
			String strand = "";
			String quality = "";
			if(IndChstart > IndChstop)
			{
				strand = "-";
				quality = ".";
			}
			else if(IndChstart < IndChstop)
			{
				strand = "+";
				quality = ".";
			}
			String label = IndChchr + "." + IndChstart + "." + IndChstop;
			out.write(UCIchr + "\t" + UCIstart + "\t" + UCIstop + "\t" + label + "\t" + quality + "\t" + strand + "\n");
			st_filtered.clear();
			dataRow = blocksFile.readLine(); // Read next line of data.
		}
		// Close the file once all data has been read.
		blocksFile.close();
		out.close();
		System.out.println("Converted nucmer to genome liftover in bed format. Saved as " + outfilebed);
		return(outfilebed);
	}

	public ArrayList<Data> readData(String inputfile) throws Exception
	{
		//Reading file containing info on chr alignments, i.e., blocks in bed6 format
		System.out.print("Loading database for coordinate conversion . . . ");
		BufferedReader blocksFile = new BufferedReader(new FileReader(inputfile));
		String dataRow = blocksFile.readLine(); // Read first line.
		ArrayList<Data> dataArray = new ArrayList<Data>();
		while (dataRow != null)
		{
			String[] st = dataRow.split("\t",-1);
			String UCIchr = st[0];
			int UCIstart = Integer.parseInt(st[1]);
			int UCIstop = Integer.parseInt(st[2]);
			String IndChdet = st[3];
			String[] IndChdetsep = IndChdet.split("\\.",-1);
			String IndChchr = IndChdetsep[0];
			int IndChstart = Integer.parseInt(IndChdetsep[1]);
			int IndChstop = Integer.parseInt(IndChdetsep[2]);
			String strand = st[5];
			dataArray.add(new Data(UCIstart, UCIstop, IndChstart, IndChstop, UCIchr, IndChchr, strand));
			dataRow = blocksFile.readLine(); // Read next line of data.
		}
		// Close the file once all data has been read.
		blocksFile.close();
		System.out.println("done");
		return(dataArray);
	}

	public void convertCoords(String Queryfile, String Outputfile, ArrayList<Data> dataArray) throws Exception
	{
		//Reading file containing query
		BufferedReader queryFile = new BufferedReader(new FileReader(Queryfile));
		String queryRow = queryFile.readLine(); // Read first line.

		//Deciding which query class to use
		String[] argQuery = Queryfile.split("\\.",-1);
		String decider = argQuery[argQuery.length-1];
		System.out.println("Query file type is: " + decider);
		System.out.print("Loading query file . . . ");
		List<Query> queryArray = new ArrayList<Query>();
		if((decider.equals("gtf")) || (decider.equals("gff")))
		{
			while (queryRow != null)
			{
				String[] st = queryRow.split("\t",-1);
				String chr = st[0];
				String program = st[1];
				String geneFeature = st[2];
				int start = Integer.parseInt(st[3]);
				int stop = Integer.parseInt(st[4]);
				String score = st[5];
				String strand = st[6];
				String frame = st[7];
				String comment = st[8];
				queryArray.add(new Query(chr, program, geneFeature, start, stop, score, strand, frame, comment));
				queryRow = queryFile.readLine(); // Read next line of data.
			}
		}
		else if(decider.equals("bed"))
		{
			while (queryRow != null)
			{
				String[] st = queryRow.split("\t",-1);
				String chr = st[0];
				int start = Integer.parseInt(st[1]);
				int stop = Integer.parseInt(st[2]);
				String comment = st[3];
				String score = st[4];
				String strand = st[5];
				queryArray.add(new Query(chr, start, stop, comment, score, strand));
				queryRow = queryFile.readLine(); // Read next line of data.
			}
		}
		// Close the file once all data has been read.
		queryFile.close();
		System.out.println("done");

		//Making the output file
		FileWriter out = new FileWriter(Outputfile);

		//parsing to find corresponding blocks for each
		System.out.println("Converting . . . ");
		for(Query query: queryArray)
		{
			int start = query.getStart();
			int stop = query.getStop();
			String chr = query.getChr();
			for(int i = 0; i < dataArray.size(); i++)
			{
				Data data = dataArray.get(i);
				if((data.getIndChchr().equals(chr)) && (data.getStrand().equals("+")) && ((start - data.getIndChstart()) >= 0) && ((stop - data.getIndChstop()) <= 0))
				{
					int offsetStart = start - data.getIndChstart();
					int offsetStop = data.getIndChstop() - stop;
					int UCIstart = data.getUCIstart() + offsetStart;
					int UCIstop = data.getUCIstop() - offsetStop;
					if((decider.equals("gtf")) || (decider.equals("gff")))
					{
						out.write(data.getUCIchr() + "\t" + query.getProgram() + "\t" + query.getGeneFeature() + "\t" + UCIstart + "\t" + UCIstop + "\t" + query.getScore() + "\t" + query.getStrand() + "\t" + query.getFrame() + "\t" + query.getComment() + "\n");
					}
					else if(decider.equals("bed"))
					{
						out.write(data.getUCIchr() + "\t" + UCIstart + "\t" + UCIstop + "\t" + query.getComment() + "\t" + query.getScore() + "\t" + query.getStrand() + "\n");
					}
					i=dataArray.size();
				}
				else if((data.getIndChchr().equals(chr)) && (data.getStrand().equals("-")) && ((start - data.getIndChstart()) <=0) && ((stop - data.getIndChstop()) >= 0))
				{
					int offsetStart = start - data.getIndChstop();
					int offsetStop = data.getIndChstart() - stop;
					int UCIstart = data.getUCIstart() + offsetStop;
					int UCIstop = data.getUCIstop() - offsetStart;
					String UCIstrand = "";
					if(query.getStrand().equals("+"))
					{
						UCIstrand = "-";
					}
					else if(query.getStrand().equals("-"))
					{
						UCIstrand = "+";
					}
					if((decider.equals("gtf")) || (decider.equals("gff")))
					{
						out.write(data.getUCIchr() + "\t" + query.getProgram() + "\t" + query.getGeneFeature() + "\t" + UCIstart + "\t" + UCIstop + "\t" + query.getScore() + "\t" + UCIstrand + "\t" + query.getFrame() + "\t" + query.getComment() + "\n");
					}
					else if(decider.equals("bed"))
					{
						out.write(data.getUCIchr() + "\t" + UCIstart + "\t" + UCIstop + "\t" + query.getComment() + "\t" + query.getScore() + "\t" + UCIstrand + "\n");
					}
					i = dataArray.size();
				}
			}
		}
		out.close();
		System.out.println("done");
		System.out.println("Converted coordinates saved in " + decider + " format in file " + Outputfile + "\n\nNote: not all features get converted and some file processing may be required before being able to view results on genome browser.");
	}


	public static void main(String args[]) throws Exception
	{
		int args_size= args.length;
		//help message
		if(args[0].equals("--help"))
		{
			System.out.println("Program to convert coordinates of newer assembly to coordinates of older assembly.\n\nUsage: java new2old liftGenome/liftGenes/liftBoth <options>\nwhere:\n\tliftGenome will use nucmer alignment of genome assemblies in tsv format to make a custom BED file that can be uploaded to a genome browser to check aligning regions of the two genomes.\n\tliftGenes will use an existing custom bed file of the genome liftOver to convert coordinates of new assembly to old assembly.\n\tliftBoth will use the nucmer alignment in tsv format to make a custom bed as well as convert gene coordinates from new assembly to old assembly.\n\nOptions:\n\t-tsv <String>\t\tThe nucmer alignment between two assemblies in tsv format (use with liftGenome or liftBoth)\n\t-bed <String>\t\tThe lifted over genome in bed format (use with liftGenes)\n\t-query <String>\t\tThe file of feature coordinates of the new assembly (use with liftGenes or liftBoth)\n\t-out <String>\t\tThe name of the output file to store converted coordinates (use with liftGenes or liftBoth)\n");
			System.exit(0);
		}
		else if(args.length == 0)
		{
			System.out.println("Program to convert coordinates of newer assembly to coordinates of older assembly.\n\nUsage: java new2old liftGenome/liftGenes/liftBoth <options>\nwhere:\n\tliftGenome will use nucmer alignment of genome assemblies in tsv format to make a custom BED file that can be uploaded to a genome browser to check aligning regions of the two genomes.\n\tliftGenes will use an existing custom bed file of the genome liftOver to convert coordinates of new assembly to old assembly.\n\tliftBoth will use the nucmer alignment in tsv format to make a custom bed as well as convert gene coordinates from new assembly to old assembly.\n\nOptions:\n\t-tsv <String>\t\tThe nucmer alignment between two assemblies in tsv format (use with liftGenome or liftBoth)\n\t-bed <String>\t\tThe lifted over genome in bed format (use with liftGenes)\n\t-query <String>\t\tThe file of feature coordinates of the new assembly (use with liftGenes or liftBoth)\n\t-out <String>\t\tThe name of the output file to store converted coordinates (use with liftGenes or liftBoth)\n");
			System.exit(0);
		}

		String Queryfile ="";
		String Outputfile ="";
		String NucmerBED ="";
		String NucmerTSV ="";
		String func ="";
		for(int elem=0;elem<args.length;elem++)
		{
			if(args[elem].equals("-query"))
			{
				Queryfile = args[elem+1];
				elem++;
				System.out.println(Queryfile);
			}
			else if(args[elem].equals("-out"))
			{
				Outputfile = args[elem+1];
				elem++;
				System.out.println(Outputfile);
			}
			else if(args[elem].equals("-bed"))
			{
				NucmerBED = args[elem+1];
				elem++;
				System.out.println(NucmerBED);
			}
			else if(args[elem].equals("-tsv"))
			{
				NucmerTSV = args[elem+1];
				elem++;
				System.out.println(NucmerTSV);
			}
			else if(args[elem].equals("liftGenome"))
			{
				func = args[elem];
				System.out.println(func);
			}
			else if(args[elem].equals("liftGenes"))
			{
				func = args[elem];
				System.out.println(func);
			}
			else if(args[elem].equals("liftBoth"))
			{
				func = args[elem];
				System.out.println(func);
			}
		}

		new2old obj = new new2old();
		//lifting genome only (liftGenome option)
		String liftedGenome = "";
		if(func.equals("liftGenome"))
		{
			liftedGenome = obj.nucmer2customBED(NucmerTSV);
		}
		//If nucmer alignment available in bed format, convert gene coordinates (liftGenes option)
		else if(func.equals("liftGenes"))
		{
			ArrayList<Data> dataArray = new ArrayList<Data>();
			dataArray=obj.readData(NucmerBED);
			obj.convertCoords(Queryfile, Outputfile, dataArray);
		}
		//If nucmer alignment available just as tsv, convert gene coordinates: (liftBoth option)
		else if(func.equals("liftBoth"))
		{
			liftedGenome = obj.nucmer2customBED(NucmerTSV);
			ArrayList<Data> dataArray = new ArrayList<Data>();
			dataArray=obj.readData(liftedGenome);
			obj.convertCoords(Queryfile, Outputfile, dataArray);
		}
	}//main
} //new2old

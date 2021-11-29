import java.util.ArrayList;
/**
 * ISTE-612-2205 Lab #2
 * Samruddhi Deshpande
 * Date : 03/11/2021
 */

public class PositionalIndex {
	String[] myDocs;
	ArrayList<String> termDictionary;                  
	ArrayList<ArrayList<Doc>> docLists;
	ArrayList<Doc> qAL1;
	ArrayList<Doc> qAL2;
	int gap = -1;

	/**
	 * Construct a positional index 
	 * @param docs List of input strings or file names
	 * 
	 */
	public PositionalIndex(String[] docs)
	{
		//TASK1: Complete the constructor of the class that builds the positional index
		myDocs=docs;
		ArrayList<Doc> single = new ArrayList<Doc>();
		docLists= new ArrayList<ArrayList<Doc>>();
		termDictionary = new ArrayList<String>();

		for(int i=0;i<myDocs.length;i++)
		{
			String[] searchwords= myDocs[i].split(" ");
			String word;

			for(int j=0;j<searchwords.length;j++) 
			{
            boolean match = false;
            word = searchwords[j];
            if(!termDictionary.contains(word)) {
               termDictionary.add(word);
               single =new ArrayList<Doc>();
               Doc doc = new Doc(i,j);
               single.add(doc);
               docLists.add(single);
            }
            else {
               int index = termDictionary.indexOf(word);
               single = docLists.get(index);
               
               int k=0;
               for(Doc did:single) {
                  if(did.docId == i) {
                     did.insertPosition(j);
                     single.set(k,did);
                     match = true;
                     break;
                  }
                  k++;
               	}
               if(!match) {
                  Doc doc = new Doc(i,j);
                  single.add(doc);
               	}
            
            	}
         	}

		}


	}
	
	/**
	 * Return string representation of a positional index
	 */
	public String toString()
	{
		String matrixString = new String();
		ArrayList<Doc> docList;
		for(int i=0;i<termDictionary.size();i++){
				matrixString += String.format("%-15s", termDictionary.get(i));
				docList = docLists.get(i);
				for(int j=0;j<docList.size();j++)
				{
					matrixString += docList.get(j)+ "\t";
				}
				matrixString += "\n";
			}
		return matrixString;
	}
	
	/**
	 * 
	 * @param post1 first postings
	 * @param post2 second postings
	 * @return merged result of two postings
	 */
	public ArrayList<Doc> intersect(ArrayList<Doc> post1, ArrayList<Doc> post2)
	{
	  ArrayList<Doc> insersecList = new ArrayList<Doc>();

      int pAL1=0, pAL2=0;
      while(pAL1<post1.size() && pAL2<post2.size()) {
         if(post1.get(pAL1).docId == post2.get(pAL2).docId) {
            ArrayList<Integer> posAL1 = post1.get(pAL1).positionList;
            ArrayList<Integer> posAL2 = post2.get(pAL2).positionList;
            
            int pposAL1 =0, pposAL2=0;
            while(pposAL1 < posAL1.size()) {
               while(pposAL2 < posAL2.size()) {
                  if(posAL1.get(pposAL1)  - posAL2.get(pposAL2) == gap) {
                  	// if(!insersecList.contains(post1.get(pAL1).docId)){
                     Doc variable = new Doc(post1.get(pAL1).docId , posAL1.get(pposAL1));
                     // System.out.println(variable);
                     insersecList.add(variable);

                  	// }
                  }
                  pposAL2++;
               }
               pposAL1++;
            }
            pAL1++;
            pAL2++;
         }
         else if(post1.get(pAL1).docId < post2.get(pAL2).docId) pAL1++;
         else pAL2++;
      }
      gap--;
      return insersecList;
	}
	
	/**
	 * 
	 * @param query a phrase query that consists of any number of terms in the sequential order
	 * @return docIds of documents that contain the phrase
	 */
	public ArrayList<Doc> phraseQuery(String[] query)
	{
		qAL1 = new ArrayList<Doc>();
		qAL2 = new ArrayList<Doc>();
		ArrayList<Doc> result = new ArrayList<Doc>();
    	// if(query.length==1){
    		// qAL1 = docLists.get(termDictionary.indexOf(query[0]));
			// return qAL1;
			result = docLists.get(termDictionary.indexOf(query[0]));
		if(query.length==1){
    		return result;
    	}
	 	else{
	    //Retrieve posting list

		// // TASK 2: Implement the intersect method that takes in two postings and output a merged posting. 

      	// 	qAL1 = docLists.get(termDictionary.indexOf(query[0]));
      	// 	qAL2 = docLists.get(termDictionary.indexOf(query[1]));
      	// 	result = intersect(qAL1,qAL2);

      	// TASK 3: Implement the phraseQuery method that takes in a phrase query with multiple terms and return a list of Doc objects.
      		// if(query[2]!= null){
      		// if(query.length>2){
			// 	for(int i=2;i<query.length;i++){
      		// 		qAL2 = docLists.get(termDictionary.indexOf(query[i]));
      		// 		result= intersect(result,qAL2);
      		// 	}
      		// }

				if(query.length>1){
					for(int i=1;i<query.length;i++){
      					qAL2 = docLists.get(termDictionary.indexOf(query[i]));
      					result= intersect(result,qAL2);
      				}
      			}
      		return result;
      	}

    }

	

	
	public static void main(String[] args)
	{
      String[] docs = {"text big opps data warehousing over big data",
                       "dimensional data warehouse over big data",
                       "nlp after text mining",
                       "nlp after text classification",
                       "nlp text before",
                       "one two three",
                       "one two"};
                       
		PositionalIndex pi = new PositionalIndex(docs);
		System.out.print(pi);

		//Phrase length 1
		String phrase = "data";
		String[] query = phrase.split(" ");
		ArrayList<Doc> one = pi.phraseQuery(query);
		// if (two==null){
		// 	System.out.println("Phrase not found in documents");
		// } 
		// else
		System.out.println("Phrase present in :\n" +phrase+" : " +one);

		//TASK4: design and test phrase queries with 2-5 terms

		//Phrase length 2
		phrase = "big data";
		String[] query2 = phrase.split(" ");
		pi.gap = -1;
		ArrayList<Doc> two = pi.phraseQuery(query2);
		System.out.println("Phrase present in :\n" +phrase+" : " +two);

		//Phrase length 3
		phrase = "nlp after text";
		String[] query3 = phrase.split(" ");
		pi.gap = -1;
		ArrayList<Doc> three = pi.phraseQuery(query3);
		System.out.println("Phrase present in :\n" +phrase+" : " +three);

		//Phrase length 4
		phrase = "text warehousing over big";
		String[] query4 = phrase.split(" ");
		pi.gap = -1;
		ArrayList<Doc> four = pi.phraseQuery(query4);
		System.out.println("Phrase present in :\n" +phrase+" : " +four);

		//Phrase length 5
		phrase = "data warehouse over big data";
		String[] query5 = phrase.split(" ");
		pi.gap = -1;
		ArrayList<Doc> five = pi.phraseQuery(query5);
		System.out.println("Phrase present in :\n" +phrase+" : " +five);
	}
}

/**
 * 
 * Document class that contains the document id and the position list
 */
class Doc{
	int docId;
	ArrayList<Integer> positionList;
	public Doc(int did)
	{
		docId = did;
		positionList = new ArrayList<Integer>();
	}
	public Doc(int did, int position)
	{
		docId = did;
		positionList = new ArrayList<Integer>();
		positionList.add(new Integer(position));
	}
	
	public void insertPosition(int position)
	{
		positionList.add(new Integer(position));
	}
	
	public String toString()
	{
		String docIdString = ""+docId + ":<";
		for(Integer pos:positionList)
			docIdString += pos + ",";
		docIdString = docIdString.substring(0,docIdString.length()-1) + ">";
		return docIdString;		
	}
}

package com.algorithm.evaluation;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class TrecEval {



	public int rTopics;//num of topics in run resultlist?????§Ö?topic????
	public int qTopics;//num of topics in qrels qrellist?????§Ö?topic????
	public int actualTopics;// actual num of topics for evaluation ??????????topic????

	public String runid; //??????????
	public Boolean graded=false;//Whether graded relevance  //??????
	public int depthM=0;//run list ???????? ??0????? result????topic???????????
	public static int DEPTH=50000; //for top ranked measures
	public static int LARGE_LENGTH=1000;// for MAP
	public static int LARGE_ENOUGH=100000;
	public static double MAX_JUDGMENT=1;//max graded relevance judgment
	/**
	 * a single result, with a pointer to relevance judgments 
	 * ???????????????????????????????????
	 * @author hcl
	 *
	 */

	public class result{
		public String docno;
		public int topic,rank;
		public double rel;
		public double score;
		public result(String docno, int topic) {
			super();
			this.docno = docno;
			this.topic = topic;
			this.rank = 0;
			this.rel = 0;
			this.score = 0;
		}
		public result(String docid, int topic, int rank, double score) {
			super();
			this.docno = docid;
			this.topic = topic;
			this.rank = rank;
			this.rel = 0;
			this.score = score;
		}
	}

	/**
	 * result list for a single topic
	 * ????????????opic??????????????????????????????????????
	 * @author hcl
	 *
	 */
	public class rList{
		public ArrayList<result> list;//result????????????????????
		public int topic;//?????????????????????????????relID
		public int nrel;//num of relevant documents for a topic//???????????qrel???????????????????????????????????????
		public double map,Rprec,recip_rank;//?????????MAP,RP,RR
		public double[] dcg,ndcg;//depth size//?????????DCG????????DCG
		public double[] err,nerr;//depth size//?????????ERR????????????????ERR??????????
		public double[] precision;//???????????P@N
		public double AP;
		public rList(int topic){
			list=new ArrayList<>();
			this.topic=topic;
			nrel=0;
			dcg=new double[DEPTH];
			ndcg=new double[DEPTH];
			err=new double[DEPTH];
			nerr=new double[DEPTH];
			precision=new double[DEPTH];
		}
	}
	/**
	 * a single qrel
	 * ???????????judge??????????
	 * @author hcl
	 *
	 */
	public class qrel{
		public String docno;
		public int topic;
		public double judgment;
		public qrel(String docno, int topic, double judgment) {
			super();
			this.docno = docno;
			this.topic = topic;
			this.judgment = judgment;
		}

	}
	/**
	 * qrel list for a single topic
	 * ????????????opic????????udge??????????
	 */
	public class qList{
		ArrayList<qrel> list;
		int topic;
		int nrel;
		double[] idealdcg;
		double[] ideallist;
		public qList(int topic){
			list=new ArrayList<>();
			this.topic=topic;
			nrel=0;
			idealdcg=new double[DEPTH];
			ideallist=new double[DEPTH];
		}
	}

	/**
	 * ??????????????????????????????????rel?????????????????opic???????????(??????????????????????????)????????????????????????????ocno???????????
	 */
	private class qrelCompare implements Comparator<qrel>{
		@Override
		public int compare(qrel aq, qrel bq) {
			if (aq.topic < bq.topic)
				return -1;
			if (aq.topic > bq.topic)
				return 1;
			return aq.docno.compareTo(bq.docno);
		}
	}
	/**
	 * ??????????????????????????????????????rel list?????????qrel???????udgment??????????????????????????????????????
	 */
	private class qrelComparebyGain implements Comparator<qrel>{
		@Override
		public int compare(qrel o1, qrel o2) {
			if (o1.judgment<o2.judgment)
				return 1;
			else if(o1.judgment> o2.judgment)
				return -1;
			else
				return 0;
		}
	}
	/**
	 * ??????????????????????result list?????????????????opic???????????????????????????????????????ocno???????????
	 * @author hcl
	 *
	 */
	private class resultCompareByDocno implements Comparator<result>{

		@Override
		public int compare(result ar, result br) {
			if (ar.topic < br.topic)
			    return -1;
			  if (ar.topic > br.topic)
			    return 1;
			  return ar.docno.compareTo(br.docno);
		}

	}
	/**
	 * ??????????????????????result list?????????????????opic???????????????????????????????????????ank???????????
	 * @author hcl
	 *
	 */
	private class resultCompareByRank implements Comparator<result>{
		@Override
		public int compare(result ar, result br) {
			if (ar.topic < br.topic)
			    return -1;
			if (ar.topic > br.topic)
			    return 1;
			return ar.rank - br.rank;
		}

	}
	/**
	 * ??????????
	 * @param r
	 * @param depthMax
	 * @return
	 */
	private ArrayList<result> applyCutoff(ArrayList<result> r, int depthMax) {
		ArrayList<result> rj=new ArrayList<>();
		int depth = -1, currentTopic = -1;
		for (int i = 0; i < r.size(); i++){
			if (r.get(i).topic != currentTopic){
				currentTopic = r.get(i).topic;
				depth = 1;
			}
			else
				depth++;
			if (depth <= depthMax){
				rj.add(r.get(i));
			}
		}
		return rj;
	}
	/**
	 * ?????????????????????????????????????????
	 * @param ql
	 */
	private void createIdealList(qList ql) {
		if(ql.nrel==0) return;
		//??????l?????????gain???????????
		Collections.sort(ql.list, new qrelComparebyGain());
		//System.out.println(ql.list.size());
		for(int i=0;i<ql.list.size();i++){
			ql.ideallist[i]=ql.list.get(i).judgment;
		}	
	}
	/**
	 * ?????????P@k
	 * @param rl
	 * @param ql 
	 */
	private void computePrecision(rList rl, qList ql){	
		if(rl.nrel==0) return;
		int count = 0;
		//****************Precision?????????????????raded-Precision*****************************
		if(graded){
			for(int i = 0; i < DEPTH ; i++){
				double gain;
				if(i<rl.list.size()) gain=rl.list.get(i).rel;
				else gain=0;
				count+=gain;
				rl.precision[i] =(double)count/((i + 1)*MAX_JUDGMENT);
				
			}		
			return;
		}
		for(int i = 0; i < DEPTH ; i++){
			if (i<rl.list.size() && rl.list.get(i).rel>0){
				count++;
			}
			rl.precision[i] =(double)count/(i + 1);
		}	
		//System.out.println(MAX_JUDGMENT);
	}
	/**
	 * ?????????MAP
	 * @param rl
	 * @param ql 
	 */
	
	private void computeMAP(rList rl, qList ql){
		if(rl.nrel==0) return;
		double count = 0.0, total = 0.0;
		rl.map=0.0;
		int length=Math.min(rl.list.size(), TrecEval.LARGE_LENGTH);
		//****************MAP?????graded-MAP*****************************
		if(graded){
			double count_best=0;
			for(int i=0;i<length;i++){
				count_best+=ql.ideallist[i];
				if(rl.list.get(i).rel>0){//???????????,??????¦Ë???????P???????AP
					count+=rl.list.get(i).rel;
					total+=count/count_best;
				}
			}
			rl.map=total/rl.nrel;
			return;
		}
		//****************************************************************


		for(int i=0;i<length;i++){
			if(rl.list.get(i).rel>0){//???????????,??????¦Ë???????P???????AP
				count++;
				total+=count/((double)(i+1));//for the map
			}
		}
		rl.map=total/rl.nrel;


	}
	/**
	 * ?????????RP
	 * @param ql 
	 */
	private void computeRprec(rList rl, qList ql){
		if(rl.nrel==0) return;
		//****************Rprec?????????????????raded-Rprec*****************************
//		if(graded){
//			int[] num_g=new int[(int) (MAX_JUDGMENT+1)];
//			int[] sum_zg=new int[(int) (MAX_JUDGMENT+1)];
//			for(int i=0;i<rl.nrel;i++){
//				int cg=ql.ideallist[i];
//				int gain;
//				if(i<rl.list.size()) gain=rl.list.get(i).rel;
//				else gain=0;
//				sum_zg[cg]+=gain;
//				num_g[cg]+=cg;
//			}
//			for(int i=(int) MAX_JUDGMENT-1;i>=1;i--){
//				sum_zg[i]+=sum_zg[i+1];
//				num_g[i]+=num_g[i+1];
//			}
//			double count=0;
//			for(int i=1;i<=(int) MAX_JUDGMENT;i++){
//				if(num_g[i]!=0)
//					count+=(double)sum_zg[i]/num_g[i];
//			}
//			rl.Rprec=count/MAX_JUDGMENT;
//			return;
//		}
		//*******************************************************************
		int count=0, length=Math.min(rl.nrel, rl.list.size());
		for(int i=0;i<length;i++){
			if (rl.list.get(i).rel>0){
				count++;
			}
		}
		rl.Rprec=(double)count/rl.nrel;
	}
	/**
	 * ?????????RR
	 */
	private void computeRR(rList rl){
		if(rl.nrel==0) return;
		int i;
		for(i=0;i<rl.list.size();i++){
			if (rl.list.get(i).rel>0){
				break;
			}
		}
		if(i<rl.list.size())
			rl.recip_rank=(double)1/(i+1);
	}
	/**
	 * ?????????dcg
	 */
	private void computeDCG(rList rl){
		if(rl.nrel==0) return;
		for(int i=0;i<DEPTH;i++){			
			if(i==0){
				double gain=rl.list.get(i).rel;
				rl.dcg[i]=(Math.pow(2.0, (double)gain)-1)/Math.log(i+2);
			}else if(i<rl.list.size()){
				double gain=rl.list.get(i).rel;
				rl.dcg[i]=rl.dcg[i-1]+(Math.pow(2.0, (double)gain)-1)/Math.log(i+2);
			}else{
				rl.dcg[i]=rl.dcg[i-1];
			}
		}
	}
	/**
	 * ???????????????????????????cg???????dealdcg
	 */
	private void computeIdealDCG(qList ql){
		if(ql.nrel==0) return;
		//??????l?????????gain???????????
		Collections.sort(ql.list, new qrelComparebyGain());
		for(int i=0;i<DEPTH;i++){		
			if(i==0){
				double gain=ql.list.get(i).judgment;
				ql.idealdcg[i]=(Math.pow(2.0, (double)gain)-1)/Math.log(i+2);
			}else if(i<ql.list.size()){
				double gain=ql.list.get(i).judgment;
				ql.idealdcg[i]=ql.idealdcg[i-1]+(Math.pow(2.0, (double)gain)-1)/Math.log(i+2);
			}else{
				ql.idealdcg[i]=ql.idealdcg[i-1];
			}
		}
	}
	/**
	 * ?????????err
	 */
	private void computeERR(rList rl){
		if(rl.nrel==0) return;
		double decay=1;
		for(int i=0;i<DEPTH;i++){
			if(i<rl.list.size()){
				double gain=rl.list.get(i).rel;
				double r=(Math.pow(2.0, (double)gain)-1)/Math.pow(2.0, MAX_JUDGMENT);
				if(i==0){
					rl.err[i]=r*decay/(i+1);
				}else{
					rl.err[i]=rl.err[i-1]+r*decay/(i+1);
				}
				decay*=(1-r);
			}else{
				rl.err[i]=rl.err[i-1];
			}
		}
	}

	/**
	 *  ?????????NDCG
	 * @param rl run list
	 * @param ql qrel list
	 */
	private void computeNDCG(rList rl, qList ql){
		if(rl.nrel==0) return;
		computeDCG(rl);
		computeIdealDCG(ql);
		for(int i=0;i<rl.ndcg.length;i++){
			if(ql.idealdcg[i]!=0){
				rl.ndcg[i]=rl.dcg[i]/ql.idealdcg[i];
			}
		}	
	}
	/**
	 * ????qrel???????????(??topic,docid????)
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public ArrayList<qList> processQrels (String filename) throws IOException {
		//1.??????qrel???????list
		ArrayList<qrel> qls=new ArrayList<>();
		BufferedReader reader=new BufferedReader(new FileReader(filename));
		String templine=null;
		int topic;
		double rel;
		String docid;
		while((templine=reader.readLine())!=null){
			StringTokenizer tokenizer=new StringTokenizer(templine);
			topic=Integer.parseInt(tokenizer.nextToken());
			tokenizer.nextToken();
			docid=tokenizer.nextToken();
			rel=Double.parseDouble(tokenizer.nextToken());
			if(rel>0){
				if(rel>MAX_JUDGMENT) MAX_JUDGMENT=rel;
				qls.add(new qrel(docid, topic, rel));
			}
		}
		reader.close();
		//2.???????topic??§³???????docid????
		Collections.sort(qls, new qrelCompare());
		//3.??topic???
		ArrayList<qList> qlists=new ArrayList<>();
		int currenttopic=-1,qcount=-1,relcount=0;
		for(int i=0;i<qls.size();i++){
			if(qls.get(i).topic!=currenttopic){
				if(qlists.size()!=0){
					qlists.get(qcount).nrel=relcount;
				}
				relcount=0;
				qcount++;
				currenttopic = qls.get(i).topic;
				qlists.add(new qList(currenttopic));
			}
			qlists.get(qcount).list.add(qls.get(i));
			relcount++;
		}
		qlists.get(qcount).nrel=relcount;
		this.qTopics=qcount+1;
		return qlists;
	}
	/**
	 * ????run?????result?????????????(??topic,docid????)
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public ArrayList<rList> processRun (File filename) throws Exception{
		//1.??????result???????list
		ArrayList<result> rls=new ArrayList<>();
		BufferedReader reader=new BufferedReader(new FileReader(filename));
		String templine=null;
		boolean needRunid = true;
		int topic,rank;
		String docid;

		while((templine=reader.readLine())!=null){
			StringTokenizer tokenizer=new StringTokenizer(templine);
			topic=Integer.parseInt(tokenizer.nextToken());
			tokenizer.nextToken();
			docid=tokenizer.nextToken();
			rank=Integer.parseInt(tokenizer.nextToken());
			tokenizer.nextToken();//score
			if (needRunid){
				this.runid = tokenizer.nextToken();
				needRunid = false;
			}
			rls.add(new result(docid, topic,rank,0));
		}
		reader.close();

		//2.??????/??????????
		/* for each topic, verify that ranks have not been duplicated */
		for (int i = 1; i < rls.size(); i++)
			if (rls.get(i).topic == rls.get(i-1).topic && rls.get(i).rank == rls.get(i-1).rank)
				throw new Exception("duplicate rank "+rls.get(i).rank+" for topic "+rls.get(i).topic+
						" in run file "+this.runid);
		/* apply depth cutoff if specified on the command line */
		if (depthM > 0) {
			rls = applyCutoff (rls, depthM);
		}
		//3.????(???topic??§³??????????docid)/???
		Collections.sort(rls, new resultCompareByDocno());

		for (int i = 1; i < rls.size(); i++)
			if (rls.get(i).topic == rls.get(i-1).topic && rls.get(i).docno.compareTo(rls.get(i-1).docno) == 0)
				throw new Exception("duplicate docno "+rls.get(i).docno+" for topic "+rls.get(i).topic+
						" in run file "+this.runid);
		//4.???
		ArrayList<rList> rlists=new ArrayList<>();
		int currenttopic=-1,qcount=-1;
		for(int i=0;i<rls.size();i++){
			if(rls.get(i).topic!=currenttopic){
				qcount++;
				currenttopic = rls.get(i).topic;
				rlists.add(new rList(currenttopic));

			}
			rlists.get(qcount).list.add(rls.get(i));
		}
		this.rTopics=qcount+1;
		return rlists;
	}




	/**
	 * ???????????????rel list ????????????????????????????????????????run list?????????????????????list???????????docid???????????????????
	 * @param q
	 * @param r
	 */
	private void applyJudgments (ArrayList<qrel> q, ArrayList<result> r){
		int i = 0, j = 0;
		while (i < q.size() && j < r.size()){
			int cmp = q.get(i).docno.compareTo(r.get(j).docno);
			if (cmp < 0)
				i++;
			else if (cmp > 0)
				j++;
			else{
				r.get(j).rel = q.get(i).judgment;
				i++;
				j++;
			}
		}
	}
	/**
	 * ??????????qrels???????un?????????????????????????????????????????????
	 * @param qrl
	 * @param rrl
	 */
	public  void applyQrels(ArrayList<qList> qrl,ArrayList<rList> rrl){
		int actualtopics=0;
		int i = 0, j = 0;
	
		while (i < qTopics && j < rTopics){
			if (qrl.get(i).topic < rrl.get(j).topic)
				i++;
			else if (qrl.get(i).topic > rrl.get(j).topic)
				j++;
			else{
				//System.out.println(qrl.get(i).topic+"  "+qrl.get(i).nrel);
				//System.out.println(rrl.get(j).list.size()+"  "+qrl.get(i).list.size());
				rrl.get(j).nrel = qrl.get(i).nrel;//???????????????????????????????
				applyJudgments(qrl.get(i).list, rrl.get(j).list);
				//????????????????????????????????????rl????????????????????docid??????????????????????????????????????????????????????????????????????????????????????????rank???????????
				Collections.sort(rrl.get(j).list, new resultCompareByRank());
				createIdealList(qrl.get(i));//???????????????????????????????????????????????Gain????????????????????????????????????
				//???????????????????????????????list??????????????????
				computePrecision (rrl.get(j),qrl.get(i));
				computeMAP (rrl.get(j),qrl.get(i));
				computeRprec(rrl.get(j),qrl.get(i));
				computeRR(rrl.get(j));
				computeNDCG(rrl.get(j), qrl.get(i));
				computeERR(rrl.get(j));
				computeAP(rrl.get(j), qrl.get(i));
				i++;
				j++;
				actualtopics++;
				//System.err.println(qrl.get(i-1).topic+"    "+actualtopics);
			}
		}
		
		this.actualTopics=actualtopics;
	}
	
	private void computeAP(rList rList, qList qList) {
		// TODO Auto-generated method stub
		/*
		 1.???L?????LDeep????????1000????1000??????
		 2.??????LDeep?????????rList????????????doc?§Ú??§Ø??????
		 3.??????LDeep?????????qList????????????doc?§Ú????????
		 4.??¨´???????topic??AP??????›ÔrList?§Ö?AP??????
		*/
		int LDeep = Math.min(rList.list.size(), DEPTH);
		double rList_rel_1 = 0 ,rList_rel_2 = 0;
		double qList_rel_1 = 0 ,qList_rel_2 = 0;
		double AP_rel_1=0.0,AP_rel_2=0.0;
		for(int i =0;i<LDeep;i++) {
			if(rList.list.get(i).rel==1) {
				rList_rel_1++;
			}else if(rList.list.get(i).rel==2) {
				rList_rel_2++;
			}
		}
		for(int i =0;i<LDeep && i<qList.list.size();i++) {
			if(qList.list.get(i).judgment==1) {
				qList_rel_1++;
			}else if(qList.list.get(i).judgment==2) {
				qList_rel_2++;
			}
		}
		if(qList_rel_1 == 0) {
			AP_rel_1 =0 ;
		}else {
			AP_rel_1 = rList_rel_1/qList_rel_1;
		}
		if(qList_rel_2 == 0) {
			AP_rel_2 =0 ;
		}else {
			AP_rel_2 = rList_rel_2/qList_rel_2;
		}
		rList.AP=(AP_rel_1 + AP_rel_2)/2;
//		System.out.println(LDeep+"\t"+rList.list.size());
//		System.out.println(qList.list.size());
//		System.out.println(rList_rel_1+"\t"+rList_rel_2);
//		System.out.println(qList_rel_1+"\t"+qList_rel_2);
//		System.out.println(this.runid+","+rList.topic+","+"AP="+rList.AP+"\n");
		
	}
	/**
	 * ?????????????????????????????????????????????????????????????????????????????????????????????topic???????????????????????????????????
	 * ???????????????????????????(MAP,Rprec,RR),(P@k,...),(NDCG@k...),(ERR@k...)
	 */
	public ArrayList<double[]> getMean(ArrayList<rList> r){
		ArrayList<double[]> means=new ArrayList<>();
		double[] MRRR_3=new double[3];//????????????????????????????????????
		double[] mean_P=new double[DEPTH];
		double[] mean_NDCG=new double[DEPTH];
		double[] meanERR=new double[DEPTH];
		for(int i=0;i<r.size();i++){
			if(r.get(i).nrel>0){
				MRRR_3[0]+=r.get(i).map;
				MRRR_3[1]+=r.get(i).Rprec;
				MRRR_3[2]+=r.get(i).recip_rank;
				for(int j=0;j<DEPTH;j++){
					mean_P[j]+=r.get(i).precision[j];
					mean_NDCG[j]+=r.get(i).ndcg[j];
					meanERR[j]+=r.get(i).err[j];
				}
			}
		}
		//System.out.println(this.actualTopics);
		/*The reason for adding 1 here is that there is a topic 
		in 2020health that has no relevant documents, so it 
		is not counted when ???????ctualTopics" is counted, but the average
		calculation needs to be divided by the overall
		*/
		//this.actualTopics = 51;//2015MB????
		MRRR_3[0]/=this.actualTopics;
		MRRR_3[1]/=this.actualTopics;
		MRRR_3[2]/=this.actualTopics;
		for(int j=0;j<DEPTH;j++){
			mean_P[j]/=this.actualTopics;
			mean_NDCG[j]/=this.actualTopics;
			meanERR[j]/=this.actualTopics;
		}
		means.add(MRRR_3);
		means.add(mean_P);
		means.add(mean_NDCG);
		means.add(meanERR);
		return means;
	}
	private boolean findArgs(String[] args, String tag){
		for(int i=0;i<args.length;i++){
			if(args[i].startsWith(tag)){
				return true; 
			}
		}
		return false;
	}
	

	
	public double trec_eval1(String qrel,File run, String outpath, String[] args) throws Exception{
		ArrayList<qList> q=processQrels(qrel);

		ArrayList<rList> r=processRun(run);
		String gradedtag="";

		applyQrels(q, r);
		ArrayList<double[]> means=getMean(r);
		BufferedWriter w=new BufferedWriter(new FileWriter(outpath));

		if(findArgs(args, "-MAP")){
			for(int i=0;i<r.size();i++){
				rList rl=r.get(i);
				w.write(this.runid+","+rl.topic+","+"MAP="+rl.map+"\n");
			}
			w.write(this.runid+",means,"+means.get(0)[0]+"\n");
			//System.out.println(/*this.runid+",MAP\t"+*/means.get(0)[0]);
		}else if(findArgs(args, "-Rprec")){		
			w.write("runid,topic,Rprec"+gradedtag+"\n");
			for(int i=0;i<r.size();i++){
				rList rl=r.get(i);
				w.write(this.runid+","+rl.topic+","+"RP="+rl.Rprec+"\n");
			}
			w.write(this.runid+",means,"+means.get(0)[1]+"\n");
			//System.err.println("finish writing "+outpath+"'s RP");
			System.out.println(/*this.runid+",RP\t"+*/means.get(0)[1]);

		}else if(findArgs(args, "-ALL")){


			double meansAP = 0.0;
			for(int i=0;i<r.size();i++) {
				rList rl = r.get(i);
				meansAP += rl.AP;
			}
			meansAP = meansAP / this.actualTopics;

			w.write("runid,IndexName,value\n");
			w.write(this.runid+",MAP,"+means.get(0)[0]+"\n");
			w.write(this.runid+",RP,"+means.get(0)[1]+"\n");
			w.write(this.runid+",AP,"+meansAP+"\n");
			w.write(this.runid+",P10,"+means.get(1)[9]+"\n");
			w.write(this.runid+",P20,"+means.get(1)[19]+"\n");
			w.write(this.runid+",NDCG10,"+means.get(2)[9]+"\n");
			w.write(this.runid+",NDCG1000,"+means.get(2)[DEPTH-1]+"\n");
			//System.out.print(this.runid+"\tMAP\t"+means.get(0)[0]+"\tP10\t"+means.get(1)[9]+"\t");
			//System.out.print(this.runid+"\tMAP\t"+means.get(0)[0]+"\tRP\t"+means.get(0)[1]+"\tAP\t"+meansAP+"\tP10\t"+means.get(1)[9]+"\tNDCG10\t"+means.get(2)[9]+"\t");
			System.out.print(this.runid+"\tMAP\t"+means.get(0)[0]+"\tRP\t"+means.get(0)[1]+"\tP10\t"+means.get(1)[9]+"\tP20\t"+means.get(1)[19]+"\t");
		}else if(findArgs(args, "-AP")){
			w.write("runid,topic,AP\n");
			double meansAP = 0.0;
			for(int i=0;i<r.size();i++) {
				rList rl = r.get(i);
				w.write(this.runid+","+rl.topic+","+"AP="+rl.AP+"\n");
				meansAP += rl.AP;
			}
			meansAP = meansAP / this.actualTopics;
			w.write(this.runid+",means,"+meansAP+"\n");
			System.out.println(this.runid+",APmeans,\t"+meansAP);
		}else if(findArgs(args, "-RR")){		
			w.write("runid,topic,RR\n");
			for(int i=0;i<r.size();i++){
				rList rl=r.get(i);
				
				w.write(this.runid+","+rl.topic+","+"RR="+rl.recip_rank+"\n");
			}
			w.write(this.runid+",means,"+means.get(0)[2]+"\n");
			
		}
		else if(findArgs(args, "-P"+gradedtag)){		
			String line="runid,topic";
			for(int j=0;j<DEPTH;j++)
				line+=",P@"+(j+1);
			w.write(line+"\n");
			for(int i=0;i<r.size();i++){
				rList rl=r.get(i);
				line=this.runid+","+rl.topic;
				for(int j=0;j<DEPTH;j++)
					line+=","+rl.precision[j];
				w.write(line+"\n");
			}
			line=this.runid+",means";
			for(int j=0;j<DEPTH;j++) {
				if(j==9) {
					line+="This";
				}
				line+=","+means.get(1)[j];
			
			}
			w.write(line+"\n");
			w.write("P@10,"+String.valueOf(means.get(1)[9])+"\n");
			w.write("P@20,"+String.valueOf(means.get(1)[19])+"\n");
			//System.err.println("finish writing "+outpath+"'s PX");
			System.out.println("P@10\t"+String.valueOf(means.get(1)[9]));
		}
		else if(findArgs(args, "-NDCG")){		
			String line="runid,topic";
			for(int j=0;j<DEPTH;j++)
				line+=",NDCG@"+(j+1);
			w.write(line+"\n");
			for(int i=0;i<r.size();i++){
				rList rl=r.get(i);
				line=this.runid+","+rl.topic;
				for(int j=0;j<DEPTH;j++)
					line+=","+rl.ndcg[j];
				w.write(line+"\n");
			}
			line=this.runid+",means";
			for(int j=0;j<DEPTH;j++) {

				line+=","+means.get(2)[j];
			}
			w.write(line+"\n");
			w.write("NDCG10,"+means.get(2)[9]+"\n");
			w.write("NDCG1000,"+means.get(2)[DEPTH-1]+"\n");
			//System.err.println("finish writing "+outpath+"'s NDCG");
			//System.out.println("NDCG1000\t"+means.get(2)[DEPTH-1]);
			System.out.println("NDCG10\t"+means.get(2)[9]);
			w.write("\n");
		}
		else if(findArgs(args, "-ERR")){		
			String line="runid,topic";
			for(int j=0;j<DEPTH;j++)
				line+=",ERR@"+(j+1);
			w.write(line+"\n");
			for(int i=0;i<r.size();i++){
				rList rl=r.get(i);
				line=this.runid+","+rl.topic;
				for(int j=0;j<DEPTH;j++)
					line+=","+rl.err[j];
				w.write(line+"\n");
			}
			line=this.runid+",means";
			for(int j=0;j<DEPTH;j++)
				line+=","+means.get(3)[j];
			w.write(line+"\n");
			w.write("ERR@20="+means.get(3)[19]+"\n");
		}else{
		//?????????????????????????????????
		w.write("runid,topic,MAP,Rprec,RR,P@5,P@10,P@20,P@100,NDCG@20,NDCG@100,NDCG@1000,ERR@20,ERR@100,ERR@1000\n");
		for(int i=0;i<r.size();i++){
			rList rl=r.get(i);
			//Zoey???????,?????????????????????????????249???????????????????????????????672????????????????????????????????????????
			if(rl.topic==672) continue;
			else
				w.write(this.runid+","+rl.topic+","+rl.map+","+rl.Rprec+","+rl.recip_rank+","+
					rl.precision[4]+","+rl.precision[9]+","+rl.precision[19]+","+rl.precision[99]+","+
					rl.ndcg[19]+","+rl.ndcg[99]+","+rl.ndcg[999]+","+
					rl.err[19]+","+rl.err[99]+","+rl.err[999]+"\n");
		}
		String meansline=this.runid+",means,"+means.get(0)[0]+","+means.get(0)[1]+","+means.get(0)[2]+","+
				means.get(1)[4]+","+means.get(1)[9]+","+means.get(1)[19]+","+means.get(1)[99]+","+
				means.get(2)[19]+","+means.get(2)[99]+","+means.get(2)[999]+","+
				means.get(3)[19]+","+means.get(3)[99]+","+means.get(3)[999];
		w.write(meansline+"\n");
		System.out.println(meansline);
		//Zoey???????
		System.out.println("finish writing");
		}
		w.close();
		//5.21¸Ä¶¯
		/*System.out.print("\t"+this.MAX_JUDGMENT+"\t");
		System.out.println("\t"+this.actualTopics+"\t");*/

		//System.out.println(means.get(0)[0]);
		return means.get(0)[0];
	}
	
	/**
	 * ??????????????????????????????????????????????
	 * @throws Exception
	 */
	public void eval() throws Exception{
		String[] args=new String[]{"-MAP"};
		//String qrel="qrels.robust2004.txt";
	    String qrel="qrels17.txt";
		//String qrel="qrels18.txt";
		//String fusionResult="E:\\fusion_prediction_data\\fusionresult";
		//String fusionResult="E:\\fusion_prediction_data\\fusionresult_mnz";
		String fusionResult="E:\\fusion_prediction_data\\fusionresult_17_mnz";
		//String fusionResult="E:\\fusion_prediction_data\\fusionresult_18";
		//String fusionResult="E:\\fusion_prediction_data\\fusionresult_17_mnz";
		//String fusionResult="E:\\fusion_prediction_data\\fusionresult_18_mnz";
		//String fusionMap="E:\\fusion_prediction_data\\fusionmap";
		//String fusionMap="E:\\fusion_prediction_data\\fusionmap_mnz";
		String fusionMap="E:\\fusion_prediction_data\\fusionmap_17_mnz";
		//String fusionMap="E:\\fusion_prediction_data\\fusionmap_17_mnz";
		//String fusionMap="E:\\fusion_prediction_data\\fusionmap_18";
		//String fusionMap="E:\\fusion_prediction_data\\fusionmap_18_mnz";
		File path=new File(fusionResult);
		File[] fusionRuns=path.listFiles();
		for(File run:fusionRuns)
			trec_eval1(qrel, run, fusionMap+"\\map_"+run.getName(), args);
		
	}
	public static void main(String[] args) throws Exception{
		args=new String[]{"-MAP"};
		TrecEval eval=new TrecEval();
		//String qrel="qrels.robust2004.txt";
		String qrel="qrels.robust2004.txt";
//		File run=new File("2004.CombMNZ");
//		String outpath="combsum.txt";
		//String fusionResult="E:\\fusion_prediction_data\\fusionresult";
		//String fusionMap="E:\\fusion_prediction_data\\fusionmap";
		String fusionResult="LCOutputFile\\10_04_2021_16_23_08_2009logadhoc_CombSUM";
		String fusionMap="LCOutputFile\\fusionmap";
		File path=new File(fusionResult);
		File[] fusionRuns=path.listFiles();
		for(File run:fusionRuns)
			eval.trec_eval1(qrel, run, fusionMap+"\\map_"+run.getName(), args);
		
	}
}

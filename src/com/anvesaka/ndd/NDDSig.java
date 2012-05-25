package com.anvesaka.ndd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NDDSig {
	
	private Set<Integer> sigHashes;
	
	/*
	 * Create a list containing the java hashcode of each of the elements of the supplied list.
	 */
	public static List<Integer> hashList(List<String> l) {
		List<Integer> hashedList = new ArrayList<Integer>(l.size());
		for (String o:l) {
			hashedList.add(o.hashCode());
		}
		return hashedList;
	}
	
	/*
	 * Generate shingles from the raw text supplied, with the specified shingle size (in characters)
	 */
	public static List<String> generateShingles(String raw, int shingleSize) {
		List<String> shingles = new ArrayList<String>(raw.length()-shingleSize);
		for (int i=0; i<raw.length()-shingleSize; i++) {
			shingles.add(raw.substring(i, i+shingleSize));
		}
		return shingles;
	}
	
	public NDDSig(String raw, int shingleSize, int resolution) {
		List<Integer> fullSignature = new ArrayList<Integer>(new HashSet<Integer>(hashList(generateShingles(raw, shingleSize))));
		Collections.sort(fullSignature);
		if (fullSignature.size()>resolution) {
			sigHashes = new HashSet<Integer>(fullSignature.subList(0, resolution));
		}
		else {
			sigHashes = new HashSet<Integer>(fullSignature);
		}
	}
	
	public double computeSimilarity(NDDSig sig) {
		int sigSize = Math.min(sigHashes.size(), sig.sigHashes.size());
		double common = 0.0;
		for (Integer shingle:sig.sigHashes) {
			if (sigHashes.contains(shingle)) {
				common++;
			}
		}
		return common/sigSize;
	}

	public static void main(String argv[]) {
		String s1 = "Vet, 77, Busted For Obama Death Threat | The Smoking Gun http://t.co/MrTUwxv via @";
		String s2 = "Vet, 77, Busted For Obama Death Threat http://tinyurl.com/25zyxgp #tcot #tlot #sgp";
		String s3 = "Playing a show in Chicago, IL at 9:00 PM today at LE PASSAGE http://artistdata.com/a/32on";
		String s4 = "Playing a show in Cape Girardeau, MO at 9:00 PM today at The Venue http://artistdata.com/a/32ow";
		NDDSig sig1 = new NDDSig(s1, 4, 10);
		NDDSig sig2 = new NDDSig(s2, 4, 10);
		NDDSig sig3 = new NDDSig(s3, 4, 10);
		NDDSig sig4 = new NDDSig(s4, 4, 10);
		System.out.println(sig1.computeSimilarity(sig2));
		System.out.println(sig3.computeSimilarity(sig4));
		System.out.println(sig1.computeSimilarity(sig4));
	}
}

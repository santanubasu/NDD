package com.anvesaka.ndd;

import java.util.*;

public class NDDSig {

    private List<Integer> sigHashes;
    private Object payload;



    public static void main(String argv[]) {
        String s1 = "Vet, 77, Busted For Obama Death Threat | The Smoking Gun http://t.co/MrTUwxv via @";
        String s2 = "Vet, 77, Busted For Obama Death Threat http://tinyurl.com/25zyxgp #tcot #tlot #sgp";
        String s3 = "Playing a show in Chicago, IL at 9:00 PM today at LE PASSAGE http://artistdata.com/a/32on";
        String s4 = "Playing a show in Cape Girardeau, MO at 9:00 PM today at The Venue http://artistdata.com/a/32ow";
        int bucketResolution = 10;
        int shingleLen = 4;
        NDDSig sig1 = new NDDSig(s1, shingleLen, bucketResolution,null);
        NDDSig sig2 = new NDDSig(s2, shingleLen, bucketResolution,null);
        NDDSig sig3 = new NDDSig(s3, shingleLen, bucketResolution,null);
        NDDSig sig4 = new NDDSig(s4, shingleLen, bucketResolution,null);
        System.out.println(sig1.computeSimilarity(sig2));
        System.out.println(sig3.computeSimilarity(sig4));
        System.out.println(sig1.computeSimilarity(sig4));
    }



    /*
     * Create a list containing the java hashcode of each of the elements of the supplied list.
     */
    public static List<Integer> hashList(List<String> l) {
        List<Integer> hashedList = new ArrayList<Integer>(l.size());
        for (String o:l) {
            int hashCode = o.hashCode();
//            System.out.println(o +" -> "+hashCode);
            hashedList.add(hashCode);
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

    public NDDSig(String raw, int shingleSize, int resolution,Object payload ) {
        this.payload = payload;

        List<String> shingles = generateShingles(raw, shingleSize);
        List<Integer> hashes = hashList(shingles);
        HashSet<Integer> integers = new HashSet<>(hashes);
        List<Integer> fullSignature = new ArrayList<Integer>(integers);
        Collections.sort(fullSignature);
        if (fullSignature.size()>resolution) {
            sigHashes = fullSignature.subList(0, resolution);
        } else {
            sigHashes = fullSignature;
        }
    }

    @Override
    public String toString(){
        if(payload != null)
            return payload.toString();
        else return "";
    }

    public double computeSimilarity(NDDSig sig) {
        int sigSize = Math.min(sigHashes.size(), sig.sigHashes.size());
        double common = 0.0;
        int i = 0, j=0;
        while( i < sigHashes.size() && j < sig.sigHashes.size()){
            Integer hash1 = sigHashes.get(i);
            Integer hash2 = sig.sigHashes.get(j);
            int comparison = hash1.compareTo(hash2);
            switch (comparison){
                case 0:
                    i++;
                    j++;
                    common++;
                    break;
                case 1:
                    j++;
                    break;
                case -1:
                    i++;
                    break;
            }
        }

        return common/sigSize;
    }

    public Object getPayload() {
        return payload;
    }

    public List<Integer> getSigHashes() {
        return sigHashes;
    }



}


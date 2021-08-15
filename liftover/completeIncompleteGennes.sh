touch IndCh_nonInv_genes.UCIcoords.new2old.clean.incompleteGenes.fullID.gff
cat listOfIncompleteGenes4IndCh_nonInv_genes.UCIcoords.new2old.clean.txt | while read incompID
do
	grep $incompID IndCh_nonInv_genes.UCIcoords.new2old.clean.incompleteGenes.gff > temp1.txt
	start=$(head -1 temp1.txt | cut -f4)
	stop=$(tail -1 temp1.txt | cut -f5)
	chrom=$(head -1 temp1.txt | cut -f1)
	aug=$(head -1 temp1.txt | cut -f2)
	strand=$(head -1 temp1.txt | cut -f7)
	mainid=$(head -1 temp1.txt | cut -f9)
	mrna="mRNA"
	dot1="."
	dot2="."
	extrainf1="ID"
	extrainf2="geneID"
	echo $mainid > temp2.txt
	transid=$(sed 's/Parent=//g' temp2.txt)
	geneid=$(sed 's/.t1//g' temp2.txt)
	label=$(echo "$extrainf1=$transid;$extrainf2=$geneid")
	echo "$chrom $aug $mrna $start $stop $dot1 $strand $dot2 $label" >> IndCh_nonInv_genes.UCIcoords.new2old.clean.incompleteGenes.fullID.gff
	cat temp1.txt >> IndCh_nonInv_genes.UCIcoords.new2old.clean.incompleteGenes.fullID.gff
done
sed -i 's/\ /\t/g' IndCh_nonInv_genes.UCIcoords.new2old.clean.incompleteGenes.fullID.gff
rm temp1.txt temp2.txt

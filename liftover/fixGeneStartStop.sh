#!bash/bin

touch IndCh_nonInv_genes.UCIcoords.fromV2code.mod5.allGenes.fixed.gff
cat IndCh_nonInv_genes.UCIcoords.fromV2code.mod5.allGenes.gff | while read chr aug feature start stop score strand num comment
do
	if [ $start -gt $stop ]
	then
		temp=$start
		start=$stop
		stop=$temp
	fi
	echo "$chr $aug $feature $start $stop $score $strand $num $comment" >> IndCh_nonInv_genes.UCIcoords.fromV2code.mod5.allGenes.fixed.gff
done
sed -i 's/\ /\t/g' IndCh_nonInv_genes.UCIcoords.fromV2code.mod5.allGenes.fixed.gff

<ideogram>
    <spacing>
     default = 0.005r
    </spacing>
        radius    = 0.9r
        thickness = 50p
        fill      = yes
        show_label       = no
        label_font       = default 
        label_radius     = 0.9r  
        label_size       = 50
        label_parallel   = yes
</ideogram>

karyotype = karyotype_inversion.txt

<image>
    <<include etc/image.conf>>
</image>

<<include etc/colors_fonts_patterns.conf>>
<<include etc/housekeeping.conf>>

<<include ticks.conf>>
    <colors>
        chr2 = blue
        indchchr2 = grey
    </colors>
#chromosomes_reverse = IndCh_Astel_HiC_Chr2R

## Hightlights
<highlights>
    <highlight>
        file       = arc.txt
        r0         = 1.08r
        r1         = 1.09r
        fill_color = black
        stroke_thickness = 2
    </highlight>
</highlights>
## Hightlights


<plots>

    <plot>
        show  = yes
        type  = scatter
        file  = data/UCIPurpleGenes.bed
        r0    = 0.30r
        r1    = 0.40r
        #max   = 1.0
        #min   = 0.0
        glyph            = circle
        glyph_size       = 10
        color            = purple
        stroke_color     = dpurple
        stroke_thickness = 5
    </plot>

    <plot>
        show  = yes
        type  = scatter
        file  = data/Track1_UCIGypsy_intersecting_with_deletions.bed
        r0    = 0.40r
        r1    = 0.48r
        #max   = 1.0
        #min   = 0.0
        glyph            = circle
        glyph_size       = 10
        color            = red
        stroke_color     = dred
        stroke_thickness = 5
    </plot>

    <plot>
        show  = yes
        type  = scatter
        file  = data/Track2_Deletions_gt5kbp.bed
        r0    = 0.52r
        r1    = 0.60r
        #min   = 12
        #max   = 17
        glyph            = circle
        glyph_size       = 10
        color            = black
        stroke_color     = dblack
        stroke_thickness = 5
    </plot>


    <plot>#indch
        show  = yes
        type  = scatter
        file  = data/IndChBrownGenes.bed
        r0    = 0.40r
        r1    = 0.50r
        #max   = 1.0
        #min   = 0.0
        glyph            = circle
        glyph_size       = 10
        color            = vvdyellow
        stroke_color     = vvdyellow
        stroke_thickness = 5
    </plot>

    <plot>#indch
        show  = yes
        type  = scatter
        file  = data/Track3_IndChGypsy_intersecting_with_insertions.bed
        r0    = 0.55r
        r1    = 0.65r
        #max   = 1.0
        #min   = 0.0
        glyph            = circle
        glyph_size       = 10
        color            = orange
        stroke_color     = dorange
        stroke_thickness = 8
    </plot>

    <plot>#indch
        show  = yes
        type  = scatter
        file  = data/Track4_Insertions_gt5kbp.bed
        r0    = 0.70r
        r1    = 0.90r
        #max   = 1.0
        #min   = 0.0
        glyph            = circle
        glyph_size       = 10
        color            = green
        stroke_color     = dgreen
        stroke_thickness = 5
    </plot>

    <plot>
        file = data/indch_2R_10k.bed
        type = heatmap
        color = ylgnbu-9-seq
        scale_log_base   = 0.5
        r0   = 0.92r
        r1   = 0.97r
        color_mapping    = 0  # default
        stroke_thickness = 0
        min              = 0
        max              = 11
    </plot>


    <plot>
        file = data/chr2_del_density.tsv
        type = heatmap
        color = ylorbr-9-seq
        scale_log_base   = 0.5
        r0   = 0.75r
        r1   = 0.80r
        color_mapping    = 0  # default
        stroke_thickness = 0
        min              = 0
        max              = 60
    </plot>
    
    <plot>
        file = data/chr2_ins_density.tsv
        type = heatmap
        color = ylorrd-9-seq
        scale_log_base   = 0.5
        r0   = 0.82r
        r1   = 0.87r
        color_mapping    = 0  # default
        stroke_thickness = 0
        min              = 0
        max              = 50
    </plot>

    <plot>
        file = data/chr2_gene_density_10k.bed
        type = heatmap
        color = ylgnbu-9-seq
        scale_log_base   = 0.5
        r0   = 0.89r
        r1   = 0.94r
        color_mapping    = 0  # default
        stroke_thickness = 0
        min              = 0
        max              = 120
    </plot>

</plots>

#!/bin/tcsh -f
set me=$0:t             		# the name of this script

# makeLists: assemble assorted lists of files from the website

set site="~/Sites/ati.svn"	# top level dir of the site
set list_dir="tech/lists/";	# where the file lists go

set all = ${list_dir}/manifest-all.txt
set all_html = ${list_dir}/manifest-all.html
# set pdf = ${list_dir}/manifest-pdf.txt
set tipitaka = ${list_dir}/URL_tipitaka.txt
set html = ${list_dir}/URL_all.txt
set lib = ${list_dir}/URL_lib.txt
set sitemap = ${list_dir}/sitemap.txt


pushd $site

touch $all_html # refresh last mod date 

set exts = ( html jpg gif png css js  pdf tcsh swf );
# jtb 20120823: removed epub and mobi from extension list

set arg = "( "

set noglob # turn off globbing for the upcoming wildcard...
echo -n "${me}: Adding files with the following extensions to the manifest: "
foreach x ( $exts )
	echo -n ".$x "
	set z = "-name *.$x -o"	#the wildcard here is just another character
	set arg = ( $arg $z )
end
echo "";

# list of any special files that might not have been captured by the previous filename extension filters
set specials = ( pdf_pali_tables.zip )
echo -n "${me}: Adding the following special files to the manifest: "
foreach x ( $specials )
	echo -n "${x} "
	set z = "-name $x -o"
	set arg = ( $arg $z )
end
echo "";

set arg = ( $arg -name dumdum \) ) # kludge! to handle the trailing -o  in the above loop

# collect the list of files and do some reformatting of ls's output...
find . $arg -exec ls -lT {} \; \
	| column -t \
	| sed -E -e "s/ +/ /g" \
	| sed -e "s/[^ ]* [^ ]* [^ ]* [^ ]* //" \
	| sed -E -e "s/ ([0-9]) / 0\1 /"\
	| sed -e "s/ Jan / 01 /" -e "s/ Feb / 02 /" -e "s/ Mar / 03 /" -e "s/ Apr / 04 /" -e "s/ May / 05 /" -e "s/ Jun / 06 /"\
	| sed -e "s/ Jul / 07 /" -e "s/ Aug / 08 /" -e "s/ Sep / 09 /" -e "s/ Oct / 10 /" -e "s/ Nov / 11 /" -e "s/ Dec / 12 /"\
	| sed -E -e "s/ ([0-9][0-9]) *([0-9][0-9]) *([0-9][0-9]:[0-9][0-9]:[0-9][0-9]) *([0-9][0-9][0-9][0-9])/ \4.\1.\2\.\3/" \
	| sed -E -e "s/([^ ]*) *([^ ]*) *([^ ]*)/\2 \3 \1/g" \
	| sed -e "s/\.\///" \
	| sed -e "/cgi\//d" \
	| column -t \
	| sort -nr \
	> ${all}.tmp

# jtb 20131202: let's not delete the 404.html 	| sed -e "/404\.html/d" \


#calculate some handy sums for the bottom
set sumLine = `awk '{s += $3} END {printf "Total: %d files, %d bytes (%.2f Mb)\n", NR, s, s/(1024*1024)}' < ${all}.tmp`

# add a low-calorie header to this huge preformatted table
ed -s ${all}.tmp << EOT
1i
Date Name Size
.
w
q
EOT

#re-columnize it
column -t ${all}.tmp > ${all}
/bin/rm ${all}.tmp

#hyperlink the files 
# DO NOT COLUMNIZE AFTER THIS POINT!
# (You don't want to include the (invisible) href tags as part of the columnizable text.)
ed -s ${all} << EOT
2,\$s/ \{1,\}\([^ ]*\) / <a href="..\/..\/\1">\1<\/a> /
w
q
EOT

# throw in the line with the tallies of file and byte counts 
echo $sumLine >> ${all}
unset noglob


#grep "\.pdf" ${all} > ${pdf}

echo "done."
#-------------------

#---- Assemble simple lists for the random link script
echo -n "bulding file lists for random links..."
find .        -name \*html | sed -e "s/^\.\/// " > $html
find tipitaka -name \*html \! -name index.html \! -name \*_utf8\* > $tipitaka
find lib      -name \*html \! -name index.html > $lib
#--------------------

ed -s ${html} << EOT
1,\$s/^/http:\/\/www.accesstoinsight.org\/
w ${sitemap}
q
EOT

#--- Assemble lists from index file links
popd
getIndexURLs.pl



echo "done."
echo "${me}: done."

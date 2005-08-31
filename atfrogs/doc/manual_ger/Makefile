# make file for PDF with Glossary and Index
#
#
MAINFILE = handbuch
TEX = latex
PDFVIEW =  acroread #xpdf
DVIVIEW = xdvi
KOMMENT = handbuchATFrogs

all: $(MAINFILE).pdf
	$(PDFVIEW) $(MAINFILE).pdf

$(MAINFILE).pdf: $(MAINFILE).tex title.tex \
	introduction.inc.tex install.inc.tex\
	login_and_categorie.inc.tex\
	groupadministration.inc.tex resourceadministration.inc.tex\
	useradministration.inc.tex troubleshooting.inc.tex\
	appendix.inc.tex
	-pdflatex -interaction=nonstopmode '$(MAINFILE).tex'
	-pdflatex -interaction=nonstopmode '$(MAINFILE).tex'
	-pdflatex -interaction=nonstopmode '$(MAINFILE).tex'
	#makeindex $(MAINFILE).glo -s glossaryformat.ist -o $(MAINFILE).gls
	-pdflatex -interaction=nonstopmode '$(MAINFILE).tex'
	pdflatex -interaction=nonstopmode '$(MAINFILE).tex'

pdf: $(MAINFILE).pdf

show: $(MAINFILE).pdf
	$(PDFVIEW) $(MAINFILE).pdf


clean:
	rm -f *~
	rm -f *.out *.aux *.log
	rm -f $(MAINFILE).dvi $(MAINFILE).ps $(MAINFILE).pdf
	rm -f $(MAINFILE)h.dvi $(MAINFILE)8.ps $(MAINFILE)8.pdf 
	rm -f $(MAINFILE)4.ps $(MAINFILE)4.pdf
	rm -f $(MAINFILE).gls $(MAINFILE).idx $(MAINFILE).glo
	rm -f $(MAINFILE).toc $(MAINFILE).ilg
	rm -f ${KOMMENT}.dvi ${KOMMENT}.ps

distclean:
	rm -f *~
	rm -f *.out *.aux *.log
	rm -f $(MAINFILE).dvi $(MAINFILE)h.dvi ${KOMMENT}.dvi
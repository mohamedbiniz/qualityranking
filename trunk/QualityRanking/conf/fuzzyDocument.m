function xCentroid=fuzzyDocument(TL, QD, cdq, qds, file)

fPoor = @(x)(trimf(x, [0/6 0/6 2/6]));
fBad = @(x)(trimf(x, [1/6 2/6 3/6]));
fRegular = @(x)(trimf(x, [2/6 3/6 4/6]));
fGood = @(x)(trimf(x, [3/6 4/6 5/6]));
fExcellent = @(x)(trimf(x, [4/6 6/6 6/6]));

funcs = {fPoor fBad fRegular fGood fExcellent};

xCentroid =0;
if (length(cdq)==QD && length(qds)==QD )
	SQERPagina = zeros(TL, QD);
	for i = 1:TL
		for j = 1:QD
			
			funcs{i}(qds(j));
			SQERPagina(i, j) = funcs{i}(qds(j));
		end
	end
	
	for i = 1:TL
	    q=0;
	    d=0;
	    for j = 1:QD
	        q = q + cdq(j)*SQERPagina(i,j);
	        d = d + cdq(j);
	    end
	    CQER(i)=q/d;
	end
	
	x = [0:0.01:1];
	
	yPoorMax = CQER(1);
	yBadMax = CQER(2);
	yRegularMax = CQER(3);
	yGoodMax = CQER(4);
	yExcellentMax = CQER(5);
	
	yPoor = fPoor(x);
	yBad = fBad(x);
	yRegular = fRegular(x);
	yGood = fGood(x);
	yExcellent = fExcellent(x);
	
	yTotal = max(min(yPoorMax, yPoor), max(min(yBadMax,yBad),max(min(yRegularMax,yRegular),max(min(yGoodMax,yGood), min(yExcellentMax, yExcellent)))));
	
	%plot(x,yPoor,':c',x,yBad,':c',x,yGood,':c',x,yExcellent,':c',x,yRegular,':c',x,yTotal,'b');
	%axis([0,1,0,1]);
	xCentroid = defuzz(x,yTotal,'centroid');
	%text(xCentroid,0.05,['\downarrow Xcentroid = ',num2str(xCentroid),' '],'HorizontalAlignment','left'); 

else
    fprintf('Erro. Argumento inválido.\n');
end
fid = fopen(file, 'wt');
fprintf(fid, '%1.20f\n', xCentroid);
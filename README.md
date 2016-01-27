# Prerequisite - MergeSort

## Split redis.csv into smaller files and sort them individually
split -l 200000 redis.csv
mkdir pars1 pars2 pars3 pars4
mv xa? xb? pars1/
mv xc? xd? pars2/
mv xe? xf? pars3/
mv xg? pars4/

vim sort.sh

```bash
#!/bin/bash
echo `date`
    for f in x* 
    do 
        cat $f | sort -k3 -t "," |  awk -F , '{print $3","$4}' > ../#$f.sort
    done 
echo `date`
```

chmod +x sort.sh

cp sort.sh pars1/
cp sort.sh pars2/
cp sort.sh pars3/
cp sort.sh pars4/

rm -f sort.sh

vim go.sh


```bash
#!/bin/bash

    for f in pars? 
    do 
        cd $f
        nohup ./sort.sh > nohup.out 2>&1 &
        cd ..
    done 
```


chmod +x go.sh

./go.sh

## merge the sorted files
 find . -name '*.sort' -print0 | sort -k1 -t, --files0-from=- -S512m -o redis.sorted.csv >sort.out 2>sort.err &

## To verify if the output file is correctly sorted.
sort -c redis.sorted.csv


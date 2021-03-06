from matplotlib import pyplot as plt
import sys

def plot_increasingSize_percentageSuccess(path, filename, output, subject, logscale=False):

    lines = [line.rstrip('\n') for line in open(path+"/"+filename)]

    labelOfN = ' '.join(lines[2].split()).split(" ")[0]
    n = ' '.join(lines[2].split()).split(" ")[3:]
    print(n)
    numberOfLocation = int(' '.join(lines[3].split()).split(" ")[0])

    percAll=[]
    nAll=[]
    indicesLocation=[]
    i = 9

    percSave = []
    nSave = []
    indiceSave = []

    while len(percAll) < 10 and i < (numberOfLocation*len(n)):#numberOfLocation:

        perc=[]
        my_n = []

        for line in lines[i:i+len(n)]:
            point = float(' '.join(line.split()).split(" ")[-1].replace(',','.'))

            if point == point:
                perc.append(point)
            else:
                perc.append(float(100.0))

            my_n.append(n[lines[i:i+len(n)].index(line)])

        if not perc in percAll and len(perc) > 0 and [p == p for p in perc] and not perc[1:] == perc[:-1]:
            indexOfLocation = ' '.join(lines[i].split()).split(" ")[1]
            indicesLocation.append(indexOfLocation)
            percAll.append(perc)
            nAll.append(my_n)
        else:
            percSave.append(perc)
            nSave.append(my_n)
            indexOfLocation = ' '.join(lines[i].split()).split(" ")[1]
            indiceSave.append(indexOfLocation)

        i+=len(n)

    while len(percAll) < 10:
        headPerc, percSave = percSave[0], percSave[1:]
        headN, nSave = nSave[0], nSave[1:]
        headIndice, indiceSave = indiceSave[0], indiceSave[1:]
        percAll.append(headPerc)
        nAll.append(headN)
        indicesLocation.append(headIndice)


    indexToCutAll = []
    for i in range(len(percAll)):
        indexToCut = len(percAll[i])-1
        while indexToCut > 1:
            if abs(percAll[i][indexToCut] - percAll[i][indexToCut-1]) < 0.5:
                indexToCut -= 1
            else:
                break;
        indexToCutAll.append(indexToCut)

    indexToCut = max(indexToCutAll)

    sortedPerc, indicesLocation = [list(x) for x in zip(*sorted(zip(percAll, indicesLocation), key=lambda pair: -pair[0][0]))]
    percAll, nAll = [list(x) for x in zip(*sorted(zip(percAll, nAll), key=lambda pair: -pair[0][0]))]

    fig = plt.figure()
    ax = fig.add_axes((.1,.4,.8,.5))
    for i in range(len(percAll)):
        cut = len(percAll[i])
        #cut = min(indexToCut, len(percAll[i]))
        plt.plot(nAll[i][:cut], percAll[i][:cut], marker='x', label=str(indicesLocation[i]+ " " + str(int(percAll[i][0])) + " %"))
    plt.xlabel(labelOfN)
    plt.ylabel("Correctness ratio")
    box = ax.get_position()
    txt = ""
    for line in lines[0:7]:
        txt += line +"\n"
    text = fig.text(.1,-.10,txt)
    plt.title(subject)
    ax.set_position([box.x0, box.y0, box.width * 0.7, box.height])
    lgd = ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    fig.savefig(path+"/img/"+output+"_plot.pdf", bbox_extra_artists=(text,), bbox_inches='tight')
    fig.savefig(path+"/img/"+output+"_plot.jpeg", bbox_extra_artists=(text,), bbox_inches='tight')
    ax.set_xscale('log')
    fig.savefig(path+"/img/"+output+"_plot_logscale.pdf", bbox_extra_artists=(text,), bbox_inches='tight')
    fig.savefig(path+"/img/"+output+"_plot_logscale.jpeg", bbox_extra_artists=(text,), bbox_inches='tight')
    plt.close(fig)

#subjects=["quicksort","md5","mersenne","zip"]
subjects=sys.argv[1:]
for subject in subjects:
    plot_increasingSize_percentageSuccess("results/"+subject, "SizeTaskExploration.txt", "sizeexplorer", subject)


import React from 'react';

import Image from 'next/image';
import styles from './Trajet.module.scss';

function renderStation(step: pathStep, index: number, size: number) {
    if (step.type === "corresp") {
        const { st1, st2, time } = step.content;
        if (st1.virtual || st2.virtual) {
            return null; // Ne pas afficher les stations virtuelles
        }
        return (
            <div className={`${styles.correspStep} ${styles.stationStep}`}>
                <div className={styles.stationIcon}>
                    <Image src={`/images/metroIcon/${st2.content.line}.svg`} alt="Metro Icon" width={40} height={40} />
                </div>
                <div className={styles.stationName}>{st2.content.displayName}</div>
                <div className={styles.correspTime}>Temps de correspondance : {time} secondes</div>
            </div>
        );
    } else if (step.type === "station") {
        const { st1, time } = step.content;
        if (st1.virtual) {
            return null; // Ne pas afficher les stations virtuelles
        }
        const isFirstStation = index === 0;
        const isLastStation = index === size - 1;
        const stationIconSize = isFirstStation || isLastStation ? 40 : 20;
        const stationNameClass = isFirstStation || isLastStation ? styles.bigStationName : styles.stationName;
        const stationStepClass = isFirstStation || isLastStation ? styles.stationStep : "";

        return (
            <div className={`${styles.stationStep} ${stationStepClass}`}>
                <div className={styles.stationIcon}>
                    <Image src={`/images/metroIcon/${st1.content.line}.svg`} alt="Metro Icon" width={stationIconSize} height={stationIconSize} />
                </div>
                <div className={stationNameClass}>{st1.content.displayName}</div>
                <div className={styles.stationTime}>{time} secondes</div>
            </div>
        );
    }
    return null;
}




type pathStepCorresp = {
    st1: StationFromBackend;
    st2: StationFromBackend;
    time: number;
}

type pathStepStation = {
    st1: StationFromBackend;
    time: number;
}

type pathStep = {
    type: "corresp";
    content: pathStepCorresp;
} | {
    type: "station";
    content: pathStepStation;
}

export default function Trajet({ RelPath, stations }: { RelPath: RelationFromBackend[], stations: StationFromBackend[] }) {
    const [stationsPath, setStationsPath] = React.useState<pathStep[]>([]);

    const [tempsTotal, setTempsTotal] = React.useState<number>(0);

    React.useEffect(() => {
        const fetchData = async () => {
            let stationsPath: pathStep[] = [];
            setTempsTotal(0);
            for (const relation of RelPath) {
                setTempsTotal((tempsTotal) => tempsTotal + relation.temps);
                const station1 = getStationById(relation.st1.id, stations);
                const station2 = getStationById(relation.st2.id, stations);

                if (station1 && station2) {
                    let corresp;
                    if (station1.virtual || station2.virtual) {
                        corresp = true;
                    } else {
                        corresp = station1.content.line != station2.content.line;
                    }
                    if (corresp) {
                        stationsPath.push({
                            type: "corresp",
                            content: {
                                st1: station1,
                                st2: station2,
                                time: relation.temps
                            }
                        });
                    } else {
                        stationsPath.push({
                            type: "station",
                            content: {
                                st1: station1,
                                time: relation.temps
                            }
                        });
                    }
                }

            }

            // vérifier dernière relation
            if (RelPath.length > 0) {
                const lastRelation = RelPath[RelPath.length - 1];
                const lastStation = getStationById(lastRelation.st2.id, stations);

                console.log("Last relation: ", lastRelation)
                console.log("Last station: ", lastStation)
                console.log("Avant last station: ", getStationById(RelPath[RelPath.length - 1].st1.id, stations))

                if (lastStation) {
                    stationsPath.push({
                        type: "station",
                        content: {
                            st1: lastStation,
                            time: 0
                        }
                    });
                }
            }

            console.log("Stations path: ", stationsPath);

            setStationsPath(stationsPath);
        }
        fetchData();
    }, [RelPath, stations])

    return <>
        <div id={styles.trajet}>
            {stationsPath.map((station, index) => renderStation(station, index, stationsPath.length))}
        </div>
        <div className="tempsTotal">
            Temps total : {Math.floor(tempsTotal / 60)} minutes et {tempsTotal % 60} secondes
        </div>
    </>;

}

function getStationById(id: string, stations: StationFromBackend[]): StationFromBackend | null {
    for (const station of stations) {
        if (station.content.id == id) {
            return station;
        }
    }
    return null;
}
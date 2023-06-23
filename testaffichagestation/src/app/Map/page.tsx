"use client";

import dynamic from 'next/dynamic';
import React from 'react';
import { GraphicCorrespondance, Station } from '../../../types/LinesAttributes';
import StationSelector from '../../../components/StationSelector';

async function getPath(start: string, end: string) {
  let res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/path?start=${start}&end=${end}`);
  let data = await res.json();
  return data;
}

async function getACM() {
  let res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/acm`);
  let data = await res.json();
  return data;
}

async function getStations(): Promise<{ stations: StationFromBackend[] }> {
  let res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/stations`, { cache: "no-cache" });

  // print caracters from 22655 to 22664 in res text
  //let txt = await res.text();
  //console.log(txt.slice(22240, 22280));
  //console.log(txt.charAt(22262));

  let data = await res.json();
  return data;
}

function removeVirtualStations(stations: StationFromBackend[]): Station[] {
  const parsed = stations.filter((station) => station.virtual === false) as RealStation[];

  return parsed.map((station) => {
    return {
      id: station.content.id.replace("Q", ""),
      idfmId: station.content.idfmId,
      idName: station.content.nameFront,
      displayName: station.content.displayName,
      line: station.content.line,
      position: {
        x: station.content.x,
        y: station.content.y
      },
      displayType: station.content.displayType as "normal" | "correspTerminus" | "correspondanceSimple" | "none" | "correspHover",
    }
  });
}

export default function Map() {

  const [path, setPath] = React.useState<number[]>([]);
  const [stations, setStations] = React.useState<StationFromBackend[]>([]);

  const [acmRelations, setAcmRelations] = React.useState<RelationFromBackend[]>([]);

  const [uniqueStations, setUniqueStations] = React.useState<StationFromBackend[]>([]);
  const [depart, setDepart] = React.useState<StationFromBackend | null>(null);
  const [arrivee, setArrivee] = React.useState<StationFromBackend | null>(null);


  React.useEffect(() => {
    //setPath(mockpath.path.map((station) => parseInt(station.slice(1))))

    const fetchData = async () => {
      //const path = await getPath("Q373", "Q145");
      //setPath(path.path.map((station: string) => parseInt(station.replace("Q", ""))));

      // afficher acm
      const acm = await getACM();

      setAcmRelations(acm.path);


      const stations = await getStations();
      setStations(stations.stations);
      console.log("AAAA");
      console.log(stations.stations);
    }

    fetchData();
  }, [])

  React.useEffect(() => {
    let uniqueStations: StationFromBackend[] = [];

    // sort pour avoir les virtuels en premiers
    const sortedStations = stations.sort((a, b) => {
      if (a.virtual === b.virtual) {
        return 0;
      } else if (a.virtual === true) {
        return -1;
      } else {
        return 1;
      }
    });

    sortedStations.forEach((station) => {
      // check by name
      if (uniqueStations.find((uniqueStation) => uniqueStation.content.name === station.content.name) === undefined) {
        uniqueStations.push(station);
      }
    });

    uniqueStations = uniqueStations.sort((a, b) => {
      if (a.content.displayName < b.content.displayName) {
        return -1;
      } else if (a.content.displayName > b.content.displayName) {
        return 1;
      } else {
        return 0;
      }
    });

    setUniqueStations(uniqueStations);

  }, [stations])

  React.useEffect(() => {

    setAcmRelations([]);

    console.log("départ : " + depart?.content.id + " / arrivée : " + arrivee?.content.id);
    if (depart !== null && arrivee !== null && depart.content.id !== arrivee.content.id) {
      getPath(depart.content.id, arrivee.content.id).then((path) => {
        setPath(path.stationsInPath.map((station: string) => parseInt(station.replace("Q", ""))));
      });
    }
  }, [depart, arrivee])

  // barrel file who call SvgComponent
  const SvgComponent = React.useMemo(() => dynamic(
    () => import('../../../components/MetroMap'),
    {
      loading: () => <p>Chargement...</p>,
      ssr: false // cette ligne est importante. Elle empêche le rendu côté serveur
    }
  ), [])


  return (
    <>
      <header>
        <h1>SAE201</h1>
      </header>
      <main>
        <aside>
          <h2>
            <span>Départ</span>
            <StationSelector stations={uniqueStations} selectedStation={depart} setSelectedStation={setDepart} />

            <span>Arrivée</span>
            <StationSelector stations={uniqueStations} selectedStation={arrivee} setSelectedStation={setArrivee} />
          </h2>
        </aside>
        <SvgComponent path={path} stations={removeVirtualStations(stations)} pathRel={acmRelations}/>
      </main>
    </>
  );
}
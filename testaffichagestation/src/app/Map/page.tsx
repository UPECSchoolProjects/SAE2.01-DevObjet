"use client";

import dynamic from 'next/dynamic';
import React from 'react';
import { GraphicCorrespondance, Station } from '../../../types/LinesAttributes';

async function getPath(start: string, end: string) {
  let res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/path?start=${start}&end=${end}`);
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
      idName: station.content.name,
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


  React.useEffect(() => {
    //setPath(mockpath.path.map((station) => parseInt(station.slice(1))))

    const fetchData = async () => {
      const path = await getPath("Q373", "Q145");
      setPath(path.path.map((station: string) => parseInt(station.replace("Q", ""))));

      const stations = await getStations();
      setStations(stations.stations);
      console.log("AAAA");
      console.log(stations.stations);
    }

    fetchData();
  }, [])

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
          <h2></h2>
        </aside>
        <SvgComponent path={path} stations={removeVirtualStations(stations)} />
      </main>
    </>
  );
}
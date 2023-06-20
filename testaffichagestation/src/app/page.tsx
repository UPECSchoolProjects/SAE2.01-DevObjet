"use client";

import dynamic from 'next/dynamic';
import React from 'react';

async function getPath(start: string, end: string) {
  let res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/path?start=${start}&end=${end}`);
  let data = await res.json();
  return data;
}

export default function Map() {

  const [path, setPath] = React.useState<number[]>([]);


  React.useEffect(() => {
    //setPath(mockpath.path.map((station) => parseInt(station.slice(1))))

    const fetchData = async () => {
      const path = await getPath("Q285", "Q217");
      setPath(path.path.map((station: string) => parseInt(station.slice(1))));
    }

    fetchData();
  }, [])

  // barrel file who call SvgComponent
  const SvgComponent = React.useMemo(() => dynamic(
    () => import('../../components/MetroMap'),
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
        <SvgComponent path={path} />

      </main>
    </>
  );
}
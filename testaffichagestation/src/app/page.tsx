import dynamic from 'next/dynamic';
import React from 'react';

export default function Map() {
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
          <h2>Aside</h2>
        </aside>
        <SvgComponent />

      </main>
    </>
  );
}
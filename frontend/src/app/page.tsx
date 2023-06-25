

const MyComponent: React.FC = () => {
  return (
    <>
      <section className="section1">
        <h1>Projet</h1>
        <p>Nous avons du développer une application orientée objet qui permet de visualiser les trajets dans le métro de manière efficace.<br /></p>
      </section>

      <section className="section2">
        <h1>Fonctionnalités</h1>
        <p>Les utilisateurs peuvent explorer les différentes lignes, les stations, les correspondances et les horaires.<br />
          Ils peuvent également rechercher des itinéraires spécifiques en indiquant leur point de départ et leur destination.<br />
          L&apos;application offre une interface conviviale et permet aux utilisateurs d&apos;enregistrer leurs trajets fréquents.<br />
          Elle est également facilement modifiable pour s&apos;adapter aux mises à jour du réseau de métro.<br />
          En somme, notre application facilite la navigation et la planification des déplacements en métro.</p>
      </section>

      <section className="section1">
        <h1>Équipe</h1>
        <ul>
          <li>Maxime LOTTO</li>
          <li>Maxence BRIAULT</li>
          <li>Chloé PARISSE</li>
          <li>Thomas DE-MOURA</li>
          <li>Antoine JARNOUX </li>
          <li>Adrien GELIS</li>
        </ul>
      </section>
    </>
  );
}

export default MyComponent;
function getFirstAndLastCommand(svgCommands) {
    const regex = /[MLHVCSQTAZmlhvcsqtaz]\s*[^MLHVCSQTAZmlhvcsqtaz]*/g;
    const matches = svgCommands.match(regex);
    
    if (!matches || matches.length === 0) {
      return null;
    }
    
    const firstCommand = matches[0];
    const lastCommand = matches[matches.length - 1];
    
    return [firstCommand, lastCommand];
  }

  function getEndPoint(command) {
    const commandType = command.charAt(0);
  
    let endPoint = [0, 0];
  
    switch (commandType) {
      case 'L':
      case 'l':
      case 'H':
      case 'h':
      case 'V':
      case 'v':
      case 'Q':
      case 'q':
      case 'T':
      case 't':
        endPoint = [parseFloat(coords[coords.length - 2]), parseFloat(coords[coords.length - 1])];
        break;
      default:
        break;
    }
  
    return endPoint;
  }
  
  // Exemple d'utilisation :
  const svgCommands1 = 'M166.480447,249.72067l30.167598-46.368715';
  const svgCommands2 = 'M196.648045,203.351955l-36.871509-55.027934';
  const svgCommands3 = 'M159.776536,148.324021q49.044692-49.324022,48.044692-48.324022';
  
  const [firstCommand1, lastCommand1] = getFirstAndLastCommand(svgCommands1);
  console.log('Première commande:', firstCommand1);
  console.log('Dernière commande:', lastCommand1);
  console.log('Coordonnées de fin:', getEndPoint(lastCommand1));

  const [firstCommand2, lastCommand2] = getFirstAndLastCommand(svgCommands2);
console.log('Première commande:', firstCommand2);
console.log('Dernière commande:', lastCommand2);
console.log('Coordonnées de fin:', getEndPoint(lastCommand2));

const [firstCommand3, lastCommand3] = getFirstAndLastCommand(svgCommands3);
console.log('Première commande:', firstCommand3);
console.log('Dernière commande:', lastCommand3);
console.log('Coordonnées de fin:', getEndPoint(lastCommand3));

// print all commands
for(i of [firstCommand1, lastCommand1, firstCommand2, lastCommand2, firstCommand3, lastCommand3]){
    console.log(i);
}
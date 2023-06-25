import React from "react";
import Select from "react-select";

export default function StationSelector({ stations, selectedStation, setSelectedStation }: { stations: StationFromBackend[]; selectedStation: StationFromBackend | null; setSelectedStation: (station: StationFromBackend) => void; }) {

    React.useEffect(() => {
        console.log("stations in selector:");
        for (const station of stations) {
            if (station.content.displayName == "") {
                console.log(station);
            }
        }
    }, [stations])

    return <div className="station-selector">
        <Select
            options={stations.map((station) => ({ value: station, label: getLabel(station) }))}
            onChange={(e: any) => setSelectedStation(e.value)}
            value={selectedStation ? { value: selectedStation, label: getLabel(selectedStation) } : null}
            placeholder="Sélectionnez une station"
            isClearable={true}
            isSearchable={true}
        />
    </div>
}

function getLabel(station: StationFromBackend) {
    if(station.virtual) {
        return station.content.displayName;
    }
    return station.content.displayName + " (" + station.content.line + ")";
}
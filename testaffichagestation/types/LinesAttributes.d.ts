export type Station = {
    id: string,
    displayName: string,
    correspondances: string[],
}

export type pathDataType = {
    linedata: { strokeColor: string, strokeWidth: string },
    troncons: (pathAttr & { id: string, beginStation: string, endStation: string })[],
    stations: { [key: string]: Station }
};


export type StopPointData = {
    point: point,
    station: Station
};


export interface pathAttr { id: string, d: string };
export type point = { x: number, y: number };
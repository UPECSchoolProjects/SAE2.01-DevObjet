interface Ligne {
    id: string;
    name: string;
    displayName: string;
}

interface VirtualContent {
    id: string;
    name: string;
    displayName: string;
    lignes: string[];
}

interface RealContent {
    id: string;
    name: string;
    idfmId: string;
    displayName: string;
    displayType: string;
    line: string;
    x: number;
    y: number;
}

interface VirtualStation {
    virtual: true;
    content: VirtualContent;
}

interface RealStation {
    virtual: false;
    content: RealContent;
}

type StationFromBackend = VirtualStation | RealStation;

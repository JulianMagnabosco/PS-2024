import {Section} from "./section";

export interface Publication {
  id: number;
  name: string;
  description: string;
  type: string;
  difficulty: string;
  sections: Section[];
  canSold: boolean;
  price: number;
  count: number
}

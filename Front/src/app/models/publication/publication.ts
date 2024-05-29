import {Section} from "./section";

export interface Publication {
  id: number;
  name: string;
  dateTime:string;
  description: string;
  type: string;
  userId: number;
  username: string;
  userIconUrl: string;
  difficulty: string;
  difficultyValue: number;
  calification: number;
  myCalification: number;
  video: string;
  sections: Section[];
  canSold: boolean;
  price: number;
  count: number
}

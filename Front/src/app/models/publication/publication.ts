export interface Publication {
  id: number;
  name: string;
  description: string;
  tags: string[];
  type: string;
  difficulty: string;
  calification: number;
  image: string;
  conditions: string[];
  materials: string[];
  steps: string[];
  canSold: boolean;
  price: number;
  count: number
}

export type ChatModel = {
  id: string;
  userId: string;
  name: string;
}

export const mockChats: ChatModel[] = [
  {
    id: '1',
    userId: 'user1',
    name: 'Chat with Alice',
  },
  {
    id: '2',
    userId: 'user2',
    name: 'Chat with Bob',
  },
];

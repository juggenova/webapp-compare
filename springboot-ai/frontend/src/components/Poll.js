import React from 'react';
import VoteOptions from './VoteOptions';

function formatDate(dateString) {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
}

function formatDateTime(dateTimeString) {
  if (!dateTimeString) return '';
  const date = new Date(dateTimeString);
  return date.toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function Poll({ poll, votes, onVoteChange }) {
  if (!poll) return null;

  return (
    <div className="bg-white p-6 rounded-lg shadow-md w-full max-w-3xl">
      <h2 className="text-2xl font-bold text-blue-700 mb-2">{poll.title}</h2>
      
      <div className="grid grid-cols-2 gap-2 mb-4 text-sm">
        <div className="text-gray-600">Description:</div>
        <div>{poll.description}</div>
        
        <div className="text-gray-600">Deadline:</div>
        <div>{formatDateTime(poll.deadline)}</div>
      </div>
      
      {poll.closed ? (
        <div>
          <p className="text-red-600 font-semibold">Voting is closed!</p>
          <p>Chosen day: {formatDate(poll.chosenDay)}</p>
        </div>
      ) : (
        <div className="mt-4">
          <h3 className="text-lg font-semibold mb-2">Cast your vote:</h3>
          {votes.map(vote => (
            <VoteOptions 
              key={vote.id} 
              vote={vote} 
              onVoteChange={onVoteChange} 
            />
          ))}
        </div>
      )}
    </div>
  );
}

export default Poll;

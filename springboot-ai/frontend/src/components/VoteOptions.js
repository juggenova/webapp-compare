import React from 'react';

function formatDate(dateString) {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    weekday: 'long',
    month: 'long',
    day: 'numeric'
  });
}

function VoteOptions({ vote, onVoteChange }) {
  const choices = ['YES', 'NO', 'MAYBE'];
  
  const handleChange = (choice) => {
    onVoteChange(vote.id, choice);
  };

  return (
    <div className="mb-4 p-3 border border-gray-200 rounded-md">
      <div className="font-medium mb-2">{formatDate(vote.day)}</div>
      <div className="flex space-x-4">
        {choices.map(choice => (
          <label key={choice} className="flex items-center cursor-pointer">
            <input
              type="radio"
              name={`vote-${vote.id}`}
              value={choice}
              checked={vote.choice === choice}
              onChange={() => handleChange(choice)}
              className="mr-1"
            />
            <span className={`
              ${choice === 'YES' ? 'text-green-600' : ''}
              ${choice === 'NO' ? 'text-red-600' : ''}
              ${choice === 'MAYBE' ? 'text-yellow-600' : ''}
            `}>
              {choice}
            </span>
          </label>
        ))}
      </div>
    </div>
  );
}

export default VoteOptions;

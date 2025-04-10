import React, { useState, useEffect } from 'react';
import Poll from './components/Poll';

function App() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pollData, setPollData] = useState(null);

  useEffect(() => {
    fetchPollData();
  }, []);

  const fetchPollData = () => {
    setLoading(true);
    fetch('/api/polls/default')
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch poll data');
        }
        return response.json();
      })
      .then(data => {
        setPollData(data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching poll data:', error);
        setError('Failed to load poll data. Please try again later.');
        setLoading(false);
      });
  };

  const handleVoteChange = (voteId, choice) => {
    fetch(`/api/polls/votes/${voteId}?choice=${choice}`, {
      method: 'POST',
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to update vote');
        }
        return response.json();
      })
      .then(updatedVote => {
        // Update the vote in the local state
        setPollData(prevData => ({
          ...prevData,
          votes: prevData.votes.map(vote =>
            vote.id === updatedVote.id ? updatedVote : vote
          )
        }));
      })
      .catch(error => {
        console.error('Error updating vote:', error);
        alert('Failed to update your vote. Please try again.');
      });
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col items-center py-10 px-4">
      <h1 className="text-3xl font-bold text-center text-blue-600 mb-8">Doodle Clone</h1>

      {loading ? (
        <div className="text-xl text-gray-600">Loading poll data...</div>
      ) : error ? (
        <div className="text-xl text-red-600">{error}</div>
      ) : (
        <Poll
          poll={pollData?.poll}
          votes={pollData?.votes}
          onVoteChange={handleVoteChange}
        />
      )}
    </div>
  );
}

export default App;
